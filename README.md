## What contains this repo?

Tests for CloudAPI v6
Example of endpoints tested:

- /datacenter
- /servers
- /nics
- /volumes
- /networkloadbalancers
- /natgateways
- /flowlogs
- /k8s
- etc.


## When the tests are be executed?
For the moment the tests are running on a Nightly Run setup on GitLab that provide a downloadable report after each run.

## Next Steps
- 1. Automatically triggered on GitHub after vdc-bundle has been deployed to staging from mainline (We need a option on how to run the cucumber tests as a part of the vdc-bundles flow)

 Other options:

- 2. Manually executed from local system.
- 3. Manually triggered to be executed in GitHub flow (with parameters against which system: KTE, Stage42)
- 4. Nightly build via GitHub against one system: KTE.

In order to achieve 1,3 and 4 we need a github runner which have acess to our KTE environment that have connection to Ionos network (VPN)

## Where to run the tests?
As part of the VDC bundle? (How to make the tests available in VDC bundle flow? jenkins-node-6 because is having VPN connection)
OR
As part of cucumber flow?


# Run Cucumber tests from Intellij Idea

## Requirements
- __Intellij Idea__ version >= 2019.1
- Latest version of __Cucumber for Java__ and __Gherkin__ plugins

## Cucumber for Java Plugin Configuration
Setup _Glue_ packages and _Program arguments_:
- **Run -> Edit Configurations**:
  - Clean any "Cucumber java" configuration instances that ran in the past
  - Inside **Templates -> Cucumber java**, setup the followings:
    - **Glue**:
      - _com.cucumber.utils_
      - _com.oneandone.ionos_
      - _com.ionos.tests_
    - **Program arguments**: _--plugin junit:output_
    - Rest of the fields, leave them as they are

# Run Cucumber tests with Maven
_Maven command_:
mvn clean verify -P{environment} (optional -Dtags=@foo)

To run on different __environment__, add __-Denv__ parameter:
```shell
mvn clean verify -Denv=stage10 -Dcleanup -Dcucumber.filter.tags="@stable"
```

## Run against your KTE

In order to select your personal KTE you can provide it via environment variable like:

```shell
KTE_NAMESPACE=prodrigues mvn clean verify -Dcucumber.filter.tags=@smoke -Dcleanup -Denv=kte -Dconcurrent=false
```
