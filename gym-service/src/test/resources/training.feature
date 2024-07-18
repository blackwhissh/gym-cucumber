Feature: Training management
  @Create
  Scenario: Adding a training
    Given I have an add training request
    When I add the training
    Then the training should be added successfully
  @Select
  Scenario: Getting a list of training types
    When I get the list of training types
    Then the list of training types should be returned successfully
  @Delete
  Scenario: Removing a training
    Given I have a training id
    When I remove the training
    Then the training should be removed successfully

