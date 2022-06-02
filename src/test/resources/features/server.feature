@stable
Feature: Server API

  Scenario: Create server as an authenticated user
    Given  User is authenticated successfully with username = alice and password = pass
    When Create server with name="Server test", cores=2 ,ram=1024, storage=8 and check response
    Then Retrieve the server with id=#[serverId]

  Scenario: Update server as an authenticated user
    Given  User is authenticated successfully with username = alice and password = pass
    And Create server with name="Server test", cores=2 ,ram=1024, storage=8 and check response
    Then Updates the server with id=#[serverId] with another server and check response body

  Scenario: Delete server as an authenticated user
    Given  User is authenticated successfully with username = alice and password = pass
    And Create server for delete with name="Server test", cores=2 ,ram=1024, storage=8 and check response
    When Delete the server with the id=#[serverId]
    Then Find the server with id=#[serverId] and check the server is no longer available

  Scenario: Update server as an authenticated admin
    Given  User is authenticated successfully with username = alice and password = pass
    And Create server with name="Server test", cores=2 ,ram=1024, storage=8 and check response
    Then User is authenticated successfully with username = admin and password = pass
    And Updates the server with id=#[serverId] with another server and check response body

  Scenario: Delete server as an authenticated admin
    Given  User is authenticated successfully with username = alice and password = pass
    Then Create server for delete with name="Server test", cores=2 ,ram=1024, storage=8 and check response
    And User is authenticated successfully with username = admin and password = pass
    When Delete the server with the id=#[serverId]
    Then Find the server with id=#[serverId] and check the server is no longer available

  Scenario: Create server as an authenticated admin
    Given  User is authenticated successfully with username = admin and password = pass
    When Create server with name="Server test", cores=2 ,ram=1024, storage=8 and check access is forbidden

  Scenario: Permission denied for access a server of another user
    Given  User is authenticated successfully with username = alice and password = pass
    And Create server with name="Server test", cores="2" ,ram="1024", storage="8" and check response
    Then User is authenticated successfully with username = ana and password = ana
    And Find server with id=#[serverId] and check access is forbidden

  Scenario: Permission denied when updating a server of another user
    Given  User is authenticated successfully with username = alice and password = pass
    And Create server with name="Server test", cores="2" ,ram="1024", storage="8" and check response
    Then User is authenticated successfully with username = ana and password = ana
    And Updates the server with id=#[serverId] with another server and check access is forbidden

  Scenario: Permission denied when deleting a server of another user
    Given  User is authenticated successfully with username = alice and password = pass
    And Create server with name="Server test", cores="2" ,ram="1024", storage="8" and check response
    Then User is authenticated successfully with username = ana and password = ana
    And Delete server with the id=#[serverId] and check access is forbidden

  Scenario: Get server as an authenticated admin
    Given  User is authenticated successfully with username = alice and password = pass
    When Create server with name="Server test", cores=2 ,ram=1024, storage=8 and check response
    Then User is authenticated successfully with username = admin and password = pass
    And Retrieve the server with id=#[serverId]

  Scenario: Get servers when user is unauthenticated
    When Access token is invalid
    Then Retrieve all Servers and  check response is {"status": 401}
    And  Retrieve  server with id=1 and  check response is {"status": 401}

  Scenario: Create server when user is unauthenticated
    When Access token is invalid
    Then Create server with id=1, name="server" cores=2 ,ram=1024, storage=8 and  check response is {"status": 401}

  Scenario: Update server when user is unauthenticated
    When Access token is invalid
    Then Update server with id=1 and check response is {"status": 401}

  Scenario: Delete server when user is unauthenticated
    When Access token is invalid
    Then Delete server with id=1 and check response is {"status": 401}