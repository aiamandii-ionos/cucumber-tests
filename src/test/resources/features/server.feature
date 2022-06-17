@stable
Feature: Server API

  Scenario Outline: Create, get by id, update and delete server as an authenticated user
    Given  User is authenticated successfully with username = <username> and password = <password>
    When Create server with name="<name>", cores=<cores> ,ram=<ram>, storage=<storage> and check response
    Then Retrieve the server with id=#[serverId]
    And Updates the server with id=#[serverId] with another server and check response body
    When Delete the server with the id=#[serverId]
    Then Find the server with id=#[serverId] and check the server is no longer available

    Examples:
      | name        | cores | ram  | storage | username | password |
      | Server test | 2     | 1024 | 8       | alice    | pass     |


  Scenario Outline: Create, get by id, update and delete server as an authenticated admin
    Given  User is authenticated successfully with username = <username> and password = <password>
    And Create server with name="<name>", cores=<cores> ,ram=<ram>, storage=<storage> and check response
    When User is authenticated successfully with username = admin and password = pass
    Then Updates the server with id=#[serverId] with another server and check response body
    And  Retrieve the server with id=#[serverId]
    When Delete the server with the id=#[serverId]
    Then Find the server with id=#[serverId] and check the server is no longer available
    When Create server with name="<name>", cores=<cores> ,ram=<ram>, storage=<storage> and check access is forbidden

    Examples:
      | name        | cores | ram  | storage | username | password |
      | Server test | 2     | 1024 | 8       | alice    | pass     |


  Scenario Outline: Access a server of another user
    Given  User is authenticated successfully with username = <username> and password = <password>
    And Create server with name="<name>", cores=<cores> ,ram=<ram>, storage=<storage> and check response
    Then User is authenticated successfully with username = ana and password = ana
    And Find server with id=#[serverId] and check access is forbidden
    And Updates the server with id=#[serverId] with another server and check access is forbidden
    And Delete server with the id=#[serverId] and check access is forbidden

    Examples:
      | name        | cores | ram  | storage | username | password |
      | Server test | 2     | 1024 | 8       | alice    | pass     |

  Scenario: User is unauthenticated
    When Access token is invalid
    Then Retrieve all Servers and  check response is {"status": 401}
    And  Retrieve  server with id=1 and  check response is {"status": 401}
    Then Create server with id=1, name="server" cores=2 ,ram=1024, storage=8 and  check response is {"status": 401}
    Then Update server with id=1 and check response is {"status": 401}
    Then Delete server with id=1 and check response is {"status": 401}