@stable
Feature: Server API

  Scenario Outline: Create, get by id, update and delete server as an authenticated user
    Given  User is authenticated successfully with username = <username> and password = <password>
    When Create server with name="<name>", cores=<cores> ,ram=<ram>, storage=<storage> and check response
    Then Check if create request has successfully completed
    And Retrieve the server with id=#[serverId]
    And Updates the server with id=#[serverId] with another server and check response body
    Then Check if update request has successfully completed
    When Delete the server with the id=#[serverId]
    Then Check if delete request has successfully completed
    And Find the server with id=#[serverId] and check the server is no longer available

    Examples:
      | name        | cores | ram  | storage | username | password |
      | Server test | 2     | 1024 | 8       | alice    | pass     |

  Scenario Outline: Create, update server when there is another delete request for the same server
    Given  User is authenticated successfully with username = <username> and password = <password>
    When Create server with name="<name>", cores=<cores> ,ram=<ram>, storage=<storage> and check response
    Then Check if create request has successfully completed
    And Delete the server with the id=#[serverId]
    Then Update the server with id=#[serverId] with another server and check request can't be created

    Examples:
      | name        | cores | ram  | storage | username | password |
      | Server test | 2     | 1024 | 8       | alice    | pass     |

  Scenario Outline: Create, delete server when there is another delete request for the same server
    Given  User is authenticated successfully with username = <username> and password = <password>
    When Create server with name="<name>", cores=<cores> ,ram=<ram>, storage=<storage> and check response
    Then Check if create request has successfully completed
    And Delete the server with the id=#[serverId]
    Then Delete the server with id=#[serverId] and check request can't be created

    Examples:
      | name        | cores | ram  | storage | username | password |
      | Server test | 2     | 1024 | 8       | alice    | pass     |

  Scenario Outline: Create, delete server when there is another update request for the same server
    Given  User is authenticated successfully with username = <username> and password = <password>
    When Create server with name="<name>", cores=<cores> ,ram=<ram>, storage=<storage> and check response
    Then Check if create request has successfully completed
    And Updates the server with id=#[serverId] with another server and check response body
    Then Delete  the server with id=#[serverId] and check request can't be created
    And Check if update request has successfully completed

    Examples:
      | name        | cores | ram  | storage | username | password |
      | Server test | 2     | 1024 | 8       | alice    | pass     |


  Scenario Outline: Create, get by id, update and delete server as an authenticated admin
    Given  User is authenticated successfully with username = <username> and password = <password>
    And Create server with name="<name>", cores=<cores> ,ram=<ram>, storage=<storage> and check response
    Then Check if create request has successfully completed
    When User is authenticated successfully with username = admin and password = admin
    Then Updates the server with id=#[serverId] with another server and check response body
    And Check if update request has successfully completed
    Then  Retrieve the server with id=#[serverId]
    When Delete the server with the id=#[serverId]
    Then Check if delete request has successfully completed
    And Find the server with id=#[serverId] and check the server is no longer available
    When Create server with name="<name>", cores=<cores> ,ram=<ram>, storage=<storage> and check access is forbidden

    Examples:
      | name        | cores | ram  | storage | username | password |
      | Server test | 2     | 1024 | 8       | alice    | pass     |


  Scenario Outline: Access a server of another user
    Given  User is authenticated successfully with username = <username> and password = <password>
    And Create server with name="<name>", cores=<cores> ,ram=<ram>, storage=<storage> and check response
    Then Check if create request has successfully completed
    Then User is authenticated successfully with username = ana and password = ana
    And Find server with id=#[serverId] and check access is forbidden
    And Updates the server with id=#[serverId] with another server and check access is forbidden
    And Delete  server with the id=#[serverId] and check access is forbidden

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

