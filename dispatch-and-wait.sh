#!/usr/bin/env bash

function trigger_workflow {
  echo "Triggering ${INPUT_EVENT_TYPE} in ${INPUT_OWNER}/${INPUT_REPO}"
  local outcome=$(curl -s "https://api.github.com/repos/${INPUT_OWNER}/${INPUT_REPO}/actions/runs?event=repository_dispatch" \
                  -H "Accept: application/vnd.github.v3+json" \
                  -H "Authorization: Bearer ${INPUT_TOKEN}")

 total_count=$(echo "${outcome}"|tr '\r\n' ' ' |jq -r ".workflow_runs | length")
 echo "$total_count"
 workflow_expect_runid="null"
 x=0
  while [ $x -le $total_count ]
   do
      workflow_name=$(echo "${outcome}"|tr '\r\n' ' ' |jq ".workflow_runs[$x].name")
      if [[ $workflow_name == '"Regression Tests"' ]]
          then
          workflow_expect_runid=$(echo "${outcome}"|tr '\r\n' ' ' |jq ".workflow_runs[$x].run_number")
          echo "I FOUND WORK ID: $workflow_expect_runid"
          break
      fi
     x=$(( $x + 1 ))
  done

  echo "Number#1 last found runId: ${workflow_expect_runid}"

  if [ "$workflow_expect_runid" = "null" ]; then
    workflow_expect_runid=0
  fi
  # we have to add + 1 to a number, although it is a string it may also be an octal (therefore we have this plus 1)
  workflow_expect_runid=$(( 1$workflow_expect_runid - 1${workflow_expect_runid//?/0}+1))
  echo "Number#2 increase for possible next workflow runId: ${workflow_expect_runid}"
  echo "Triggering workflow dispatched on ${INPUT_EVENT_TYPE} in ${INPUT_OWNER}/${INPUT_REPO}"
  echo "Payload ${INPUT_CLIENT_PAYLOAD}"
  local resp=$(curl -w '%{response_code}' -s -o /dev/null -X POST -i "https://api.github.com/repos/${INPUT_OWNER}/${INPUT_REPO}/dispatches" \
    -H "Accept: application/vnd.github.v3+json" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer ${INPUT_TOKEN}" \
    -d "{\"event_type\": \"${INPUT_EVENT_TYPE}\", \"client_payload\": ${INPUT_CLIENT_PAYLOAD} }")

  if [ -n "$resp" ] && [ "$resp" -lt 400 ]
  then
    echo "Returned response code is: ${resp:-ok} - all good. Sleeping and waiting for next call to check status"
    sleep 2
  else
    echo "Workflow failed to trigger"
    echo "$resp"
    echo "Printing also outcome of first call: "
    echo "$outcome"
    exit 1
  fi
}

function ensure_workflow {
  max_wait=10
  stime=$(date +%s)
  while [ $(( `date +%s` - $stime )) -lt $max_wait ]
  do
    echo "Ensuring workflow is running with expected run id: ${workflow_expect_runid}"
    local result=$(curl -s "https://api.github.com/repos/${INPUT_OWNER}/${INPUT_REPO}/actions/runs?event=repository_dispatch" \
                         -H "Accept: application/vnd.github.v3+json" \
                         -H "Authorization: Bearer ${INPUT_TOKEN}")

    workflow_runid=$(echo "${result}"|tr '\r\n' ' ' |jq ".workflow_runs[] | select(.run_number==$workflow_expect_runid) | .id")

    [ -n "${workflow_runid}" ] && echo "Workflow runId found. Monitoring ${workflow_runid}"
    [ -z "${workflow_runid}" ] || break
     echo echo "Workflow runId not yet found. Monitoring further for ${workflow_expect_runid}"
    sleep 2
  done

  if [ -z "${workflow_runid}" ]; then
    >&2 echo "No workflow run id found. Repository dispatch failed!"
    exit 1
  fi

  echo "Workflow run id is ${workflow_runid}"
}

function wait_on_workflow {
  stime=$(date +%s)
  conclusion="null"

  echo "Dispatched workflow run URL:"
  echo -n "==> "
  local url=$(curl -s "https://api.github.com/repos/${INPUT_OWNER}/${INPUT_REPO}/actions/runs/${workflow_runid}" \
    -H "Accept: application/vnd.github.v3+json" \
    -H "Authorization: Bearer ${INPUT_TOKEN}"|tr '\r\n' ' ' |jq -r '.html_url')
  echo -n "(sleep time: ${INPUT_WAIT_TIME} in seconds, "
  echo "max time: ${INPUT_MAX_TIME} in seconds)"
  while [[ $conclusion == "null" ]]
  do
    rtime=$(( `date +%s` - $stime ))
    if [[ "$rtime" -ge "$INPUT_MAX_TIME" ]]
    then
      echo "==> Time limit exceeded. Letting workflow fail."
      exit 1
    fi
    sleep $INPUT_WAIT_TIME
    echo "Sleeping is over, retrying. Elasped time already: ${rtime} seconds before reaching max time of ${INPUT_MAX_TIME} seconds"
    conclusion=$(curl -s "https://api.github.com/repos/${INPUT_OWNER}/${INPUT_REPO}/actions/runs/${workflow_runid}" \
    	-H "Accept: application/vnd.github.v3+json" \
    	-H "Authorization: Bearer ${INPUT_TOKEN}"|tr '\r\n' ' ' |jq -r '.conclusion')

    if [ "$conclusion" == "failure" ]; then
      break
    fi
  done

  if [[ $conclusion == "success" ]]
  then
    echo "Success on workflow ${url}"
  else
    echo "Failed (conclusion: $conclusion) for workflow ${url}!"
    exit 1
  fi
}

function main {
  trigger_workflow
  ensure_workflow
  wait_on_workflow
}

main