Feature: Trainee management
  Scenario: Creating a trainee profile
    Given I have a trainee registration request
    When I create the trainee profile
    Then the trainee profile should be created successfully

  Scenario: Creating a trainee profile with missing information
    Given I have a trainee registration request with missing information
    When I attempt to create the trainee profile
    Then the trainee profile creation should fail with an error

  Scenario: Selecting a trainee profile
    Given I have a username of an existing trainee
    When I select the trainee profile
    Then the trainee profile should be returned successfully

  Scenario: Updating a trainee profile
    Given I have a username of an existing trainee and an update request
    When I update the trainee profile
    Then the trainee profile should be updated successfully

  Scenario: Deleting a trainee profile
    Given I have a username of an existing trainee
    When I delete the trainee profile
    Then the trainee profile should be deleted successfully

  Scenario: Getting a list of trainings by trainee
    Given I have a username of an existing trainee and a training request
    When I get trainee's training list
    Then trainee's training list should be returned successfully

  Scenario: Getting a list of not assigned trainers for a trainee
    Given I have a username of an existing trainee
    When I get the list of not assigned trainers
    Then the list of not assigned trainers should be returned successfully

  Scenario: Getting a list of assigned trainers for a trainee
    Given I have a username of an existing trainee
    When I get the list of assigned trainers
    Then the list of assigned trainers should be returned successfully

  Scenario: Updating the list of trainers for a trainee
    Given I have a username of an existing trainee and a list of trainers
    When I update the list of trainers
    Then the list of trainers should be updated successfully
