Feature: User management
  @Login
  Scenario: Successful authentication
    Given I have a username and password
    When I login with valid credentials
    Then I should be authenticated successfully
  @Login
  Scenario: Unsuccessful authentication
    Given I have a username and password
    When I login with invalid credentials
    Then authentication should be unsuccessful
  @Update
  Scenario: Password Change
    Given I have a valid username and user info
    When I change password
    Then password should be changed successfully
  @Update
  Scenario: Activate/Deactivate user
    Given I have a valid username
    When I activate or deactivate user
    Then user should be activated or deactivated successfully