Feature: Workload management
  Scenario: Getting a summary of a trainer
    Given I have a username of an existing trainer
    When I get the trainer summary
    Then the trainer summary should be returned successfully
  Scenario: Getting a summary of a trainer by month and year
    Given I have a username of an existing trainer and a year and a month
    When I get the trainer summary by month and year
    Then the trainer summary by month and year should be returned successfully
