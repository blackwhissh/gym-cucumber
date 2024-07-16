Feature: Trainer management
  Scenario: Creating a trainer profile
    Given I have a trainer registration request
    When I create the trainer profile
    Then the trainer profile should be created successfully

  Scenario: Creating a trainer profile with missing information
    Given I have a trainer registration request with missing information
    When I attempt to create the trainer profile
    Then the trainer profile creation should fail with an error