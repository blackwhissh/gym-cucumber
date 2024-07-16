Feature: Trainee management
  Scenario: Creating a trainee profile
    Given I have a trainee registration request
    When I create the trainee profile
    Then the trainee profile should be created successfully

  Scenario: Creating a trainee profile with missing information
    Given I have a trainee registration request with missing information
    When I attempt to create the trainee profile
    Then the trainee profile creation should fail with an error