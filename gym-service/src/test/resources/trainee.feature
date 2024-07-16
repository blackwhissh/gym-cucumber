Feature: Trainee management
  Scenario: Creating a trainee profile
    Given I have a trainee registration request
    When I create the trainee profile
    Then the trainee profile should be created successfully
