package com.ase.bytealchemists.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ase.bytealchemists.config.TestSecurityConfig;
import com.ase.bytealchemists.controller.UserController;
import com.ase.bytealchemists.model.UserEntity;
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
}
