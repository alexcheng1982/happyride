Feature: Address management
  Manage a passenger's addresses

  Scenario: Add a new address
    Given a passenger with 1 addresses
    When the passenger adds a new address
    Then the passenger has 2 addresses

  Scenario: Delete an address
    Given a passenger with 3 addresses
    When the passenger deletes an address
    Then the passenger has 2 addresses