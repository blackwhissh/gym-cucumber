Feature: Trainer management
  Scenario: Creating a trainer profile
    Given I have a trainer registration request
    When I create the trainer profile
    Then the trainer profile should be created successfully

  Scenario: Creating a trainer profile with missing information
    Given I have a trainer registration request with missing information
    When I attempt to create the trainer profile
    Then the trainer profile creation should fail with an error

  Scenario: Selecting a trainer profile
    Given I have a username of an existing trainer
    When I select the trainer profile
    Then the trainer profile should be returned successfully

  Scenario: Updating a trainer profile
    Given I have a username of an existing trainer and an update request
    When I update the trainer profile
    Then the trainer profile should be updated successfully

  Scenario: Getting a list of trainings by a trainer
    Given I have a username of an existing trainer and a training request
    When I get the training list
    Then the training list should be returned successfully

  Scenario: Selecting non-existing trainer profile
    Given I have a username of a non-existing trainer
    When I attempt to select trainer profile
    Then the trainer profile selection should fail with an error

  Scenario: Updating non-existing trainer profile
    Given I have a username of a non-existing trainer
    When I attempt to update trainer profile
    Then the trainer profile update should fail with an error