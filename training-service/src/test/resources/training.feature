Feature: Training management
  @Workload
  Scenario: Updating workload of a trainer
    Given I have a username, first name, last name, status, duration, training date, and action type
    When I update the workload
    Then the workload should be updated successfully