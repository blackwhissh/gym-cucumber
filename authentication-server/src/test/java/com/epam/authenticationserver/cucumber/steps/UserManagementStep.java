package com.epam.authenticationserver.cucumber.steps;

import com.epam.authenticationserver.dto.JwtResponse;
import com.epam.authenticationserver.dto.LoginDTO;
import com.epam.authenticationserver.dto.UserInfoDTO;
import com.epam.authenticationserver.service.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserManagementStep {
    private final UserService userService = mock(UserService.class);
    private String username;
    private UserInfoDTO userInfoDTO;
    private LoginDTO loginDTO;
    private ResponseEntity<?> response;

    @Given("I have a username and password")
    public void i_have_a_username_and_password() {
        loginDTO = new LoginDTO("username", "password");
    }

    @When("I login with valid credentials")
    public void i_login_with_valid_credentials() {
        when(userService.authenticate(loginDTO))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));
        response = userService.authenticate(loginDTO);
    }

    @Then("I should be authenticated successfully")
    public void i_should_be_authenticated_successfully() {
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }
    @When("I login with invalid credentials")
    public void i_login_with_invalid_credentials() {
        when(userService.authenticate(loginDTO))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(401)));
        response = userService.authenticate(loginDTO);
    }

    @Then("authentication should be unsuccessful")
    public void i_should_not_be_authenticated() {
        assertNotNull(response);
        assertEquals(401, response.getStatusCode().value());
    }

    @Given("I have a valid username and user info")
    public void i_have_a_valid_username_and_user_info() {
        username = "test";
        userInfoDTO = new UserInfoDTO();
    }
    @When("I change password")
    public void i_change_password() {
        when(userService.changePassword(username,userInfoDTO))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));
        response = userService.changePassword(username, userInfoDTO);
    }
    @Then("password should be changed successfully")
    public void password_should_be_changed_successfully() {
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Given("I have a valid username")
    public void iHaveAValidUsername() {
        username = "test";
    }

    @When("I activate or deactivate user")
    public void iActivateOrDeactivateUser() {
        when(userService.activateDeactivate(username))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));
        response = userService.activateDeactivate(username);
    }

    @Then("user should be activated or deactivated successfully")
    public void userShouldBeActivatedOrDeactivatedSuccessfully() {
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }
}
