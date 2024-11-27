package com.ase.bytealchemists.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ase.bytealchemists.config.TestSecurityConfig;
import com.ase.bytealchemists.controller.UserController;
import com.ase.bytealchemists.model.UserEntity;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


/**
 * This class contains the unit tests for the UserController class.
 */
@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  /**
   * Test for successful user signup.
   */
  @Test
  public void testSignupSuccess() throws Exception {
    UserEntity mockUser = new UserEntity();
    mockUser.setUsername("johndoe");
    mockUser.setFirstName("John");
    mockUser.setLastName("Doe");
    mockUser.setPassword("securepassword");
    mockUser.setEmail("johndoe@example.com");
    mockUser.setPhone("+1234567890");

    // Mocking service behavior
    when(userService.registerUser(any(UserEntity.class))).thenReturn(true);

    // Perform POST request
    mockMvc.perform(post("/user/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                        {
                          "username": "johndoe",
                          "firstName": "John",
                          "lastName": "Doe",
                          "password": "securepassword",
                          "email": "johndoe@example.com",
                          "phone": "+1234567890"
                        }
                        """))
        .andExpect(status().isCreated())
        .andExpect(content().string("User registered successfully."));

    verify(userService, times(1)).registerUser(any(UserEntity.class));
  }

  /**
   * Test for user signup when username or email already exists.
   */
  @Test
  public void testSignupConflict() throws Exception {
    when(userService.registerUser(any(UserEntity.class))).thenReturn(false);

    mockMvc.perform(post("/user/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                        {
                          "username": "johndoe",
                          "firstName": "John",
                          "lastName": "Doe",
                          "password": "securepassword",
                          "email": "johndoe@example.com",
                          "phone": "+1234567890"
                        }
                        """))
        .andExpect(status().isConflict())
        .andExpect(content().string("Username or email already exists."));

    verify(userService, times(1)).registerUser(any(UserEntity.class));
  }

  /**
   * Test for user signup when an internal server error occurs.
   */
  @Test
  public void testSignupInternalServerError() throws Exception {
    when(userService.registerUser(any(UserEntity.class)))
        .thenThrow(new RuntimeException("Database error"));

    mockMvc.perform(post("/user/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                        {
                          "username": "johndoe",
                          "firstName": "John",
                          "lastName": "Doe",
                          "password": "securepassword",
                          "email": "johndoe@example.com",
                          "phone": "+1234567890"
                        }
                        """))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("An error occurred while registering the user."));

    verify(userService, times(1)).registerUser(any(UserEntity.class));
  }

  /**
   * Test for user signup when the input is invalid.
   */
  @Test
  public void testSignupInvalidInput() throws Exception {
    mockMvc.perform(post("/user/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                        {
                          "username": "",
                          "firstName": "John",
                          "lastName": "Doe",
                          "password": "securepassword",
                          "email": "invalid-email",
                          "phone": "+1234567890"
                        }
                        """))
        .andExpect(status().isBadRequest());

    verify(userService, times(0)).registerUser(any(UserEntity.class));
  }

  /**
   * Tests successful user login.
   *
   * @throws Exception if any error occurs during the test execution.
   */
  @Test
  public void testLoginSuccess() throws Exception {
    UserEntity mockUser = new UserEntity();
    mockUser.setId(1L);
    mockUser.setUsername("johndoe");
    mockUser.setPassword("encryptedpassword");

    // Mock userService.findUserByUsername()
    when(userService.findUserByUsername("johndoe")).thenReturn(Optional.of(mockUser));

    // Mock userService.verifyPassword()
    when(userService.verifyPassword("securepassword", "encryptedpassword")).thenReturn(true);

    mockMvc.perform(post("/user/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                          "username": "johndoe",
                          "password": "securepassword"
                        }
                        """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.message").value("Login successful"));

    verify(userService, times(1)).findUserByUsername("johndoe");
    verify(userService, times(1)).verifyPassword("securepassword", "encryptedpassword");
  }

  /**
   * Tests the scenario where login fails due to incorrect password.
   *
   * @throws Exception if any error occurs during the test execution.
   */
  @Test
  public void testLoginFailureInvalidCredentials() throws Exception {
    // Create a mock UserEntity object
    UserEntity mockUser = new UserEntity();
    mockUser.setUsername("johndoe");
    mockUser.setPassword("encryptedpassword");

    // Mock userService.findUserByUsername() method
    when(userService.findUserByUsername("johndoe")).thenReturn(Optional.of(mockUser));

    // Mock userService.verifyPassword() method to simulate password mismatch
    when(userService.verifyPassword("wrongpassword", "encryptedpassword")).thenReturn(false);

    // Execute POST request
    mockMvc.perform(post("/user/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                          "username": "johndoe",
                          "password": "wrongpassword"
                        }
                        """))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Invalid credentials"));

    // Verify method invocation counts
    verify(userService, times(1)).findUserByUsername("johndoe");
    verify(userService, times(1)).verifyPassword("wrongpassword", "encryptedpassword");
  }

  /**
   * Tests the scenario where login fails because the user does not exist.
   *
   * @throws Exception if any error occurs during the test execution.
   */
  @Test
  public void testLoginFailureUserNotFound() throws Exception {
    // Mock userService.findUserByUsername() method to simulate user not found
    when(userService.findUserByUsername("unknownuser")).thenReturn(Optional.empty());

    // Execute POST request
    mockMvc.perform(post("/user/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                          "username": "unknownuser",
                          "password": "somepassword"
                        }
                        """))
            .andExpect(status().isNotFound())
            .andExpect(content().string("User not found"));

    // Verify method invocation counts
    verify(userService, times(1)).findUserByUsername("unknownuser");
    verify(userService, times(0)).verifyPassword(any(String.class), any(String.class));
  }

  /**
   * Tests the scenario where invalid input is provided during login (missing username).
   *
   * @throws Exception if any error occurs during the test execution.
   */
  @Test
  public void testLoginInvalidInput() throws Exception {
    mockMvc.perform(post("/user/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                          "username": "",
                          "password": "securepassword"
                        }
                        """))
            .andExpect(status().isBadRequest());

    verify(userService, times(0)).findUserByUsername(any(String.class));
    verify(userService, times(0)).verifyPassword(any(String.class), any(String.class));
  }

  /**
   * Tests the successful password reset scenario.
   *
   * @throws Exception if any error occurs during the test execution.
   */
  @Test
  public void testResetPasswordSuccess() throws Exception {
    doThrow(new IllegalArgumentException("")).when(userService).resetPassword("johndoe",
        "NewPassword@123");

    mockMvc.perform(post("/user/resetPassword")
            .param("username", "johndoe")
            .param("newPassword", "NewPassword@456"))
        .andExpect(status().isOk())
        .andExpect(content().string("Password updated successfully."));

    verify(userService, times(1)).resetPassword("johndoe", "NewPassword@456");
  }

  /**
   * Tests the password reset scenario when the user is not found.
   *
   * @throws Exception if any error occurs during the test execution.
   */
  @Test
  public void testResetPasswordUserNotFound() throws Exception {
    doThrow(new IllegalArgumentException("User not found")).when(userService).resetPassword(
        "unknownuser", "NewPassword@123");

    mockMvc.perform(post("/user/resetPassword")
            .param("username", "unknownuser")
            .param("newPassword", "NewPassword@123"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("User not found"));

    verify(userService, times(1)).resetPassword("unknownuser", "NewPassword@123");
  }

  /**
   * Tests the scenario where no username is provided for password reset.
   *
   * @throws Exception if any error occurs during the test execution.
   */
  @Test
  public void testResetPasswordMissingUsername() throws Exception {
    mockMvc.perform(post("/user/resetPassword")
            .param("newPassword", "NewPassword@123"))
        .andExpect(status().isBadRequest());

    verify(userService, times(0)).resetPassword(any(String.class), any(String.class));
  }

  /**
   * Tests the scenario where no new password is provided for password reset.
   *
   * @throws Exception if any error occurs during the test execution.
   */
  @Test
  public void testResetPasswordMissingPassword() throws Exception {
    mockMvc.perform(post("/user/resetPassword")
            .param("username", "johndoe"))
        .andExpect(status().isBadRequest());

    verify(userService, times(0)).resetPassword(any(String.class), any(String.class));
  }

  /**
   * Test for deleteUserByName() method when username deleted successfully.
   */
  @Test
  public void testDeleteUserByIdWhenSuccess() throws Exception {
    when(userService.deleteUserByName("Peter")).thenReturn(true);

    mockMvc.perform(delete("/user/{username}", "Peter")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("User was deleted successfully."));

    verify(userService, times(1)).deleteUserByName("Peter");
  }

  /**
   * Test for deleteUserByName() method when username does not exist.
   */
  @Test
  public void testDeleteUserByIdWhenFail() throws Exception {
    when(userService.deleteUserByName("Peter")).thenReturn(false);

    mockMvc.perform(delete("/user/{username}", "Peter")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("This user does not exist."));

    verify(userService, times(1)).deleteUserByName("Peter");
  }

  /**
   * Test for deleteUserByName() method when there is exception.
   */
  @Test
  public void testDeleteUserByIdWhenExceptionExists() throws Exception {
    when(userService.deleteUserByName("Peter")).thenThrow(new RuntimeException("Database error"));

    mockMvc.perform(delete("/user/{username}", "Peter")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("An Error has occurred."));

    verify(userService, times(1)).deleteUserByName("Peter");
  }

}
