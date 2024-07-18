Feature: Training management
  Scenario: Adding a training
    Given I have an add training request
    When I add the training
    Then the training should be added successfully