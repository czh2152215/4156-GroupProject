package com.ase.bytealchemists.controller;

import com.ase.bytealchemists.model.UserEntity;
import com.ase.bytealchemists.service.UserService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Class UserController.
 *
 * @author Jason
 * @version 2024/11/21
 */
@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  private UserService userService;

  /**
   * Endpoint for user signup.
   *
   * @param userEntity The user details for creating an account.
   * @return A {@code ResponseEntity} indicating the status of the operation.
   */
  @PostMapping("/signup")
  public ResponseEntity<?> signup(@Valid @RequestBody UserEntity userEntity) {
    try {
      boolean isRegistered = userService.registerUser(userEntity);
      if (isRegistered) {
        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
      } else {
        return new ResponseEntity<>("Username or email already exists.", HttpStatus.CONFLICT);
      }
    } catch (Exception e) {
      return new ResponseEntity<>("An error occurred while registering the user.",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Endpoint for user login.
   *
   * <p>This method handles POST requests to "/user/login" and attempts to authenticate the user
   * using the provided username and password. It manually validates the required fields
   * and returns appropriate HTTP responses based on the authentication result.
   *
   * @param loginRequest The user login details containing username and password.
   * @return A {@code ResponseEntity} indicating the result of the login attempt.
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserEntity loginRequest) {
    // Manually validate required fields
    if (loginRequest.getUsername() == null || loginRequest.getUsername().isBlank()) {
      return ResponseEntity.badRequest().body("Username cannot be blank");
    }
    if (loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
      return ResponseEntity.badRequest().body("Password cannot be blank");
    }

    try {
      // Check if the user exists
      Optional<UserEntity> userOptional = userService
              .findUserByUsername(loginRequest.getUsername());
      if (userOptional.isPresent()) {
        UserEntity user = userOptional.get();

        // Verify password
        boolean passwordMatches = userService.verifyPassword(loginRequest.getPassword(),
                user.getPassword());
        if (passwordMatches) {
          return ResponseEntity.ok("Login successful");
        } else {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
      }
    } catch (Exception e) {
      return new ResponseEntity<>("An error occurred during login",
              HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/forgotPassword")
  public ResponseEntity<?> forgotPassword(@RequestParam String username) {
    try {
      // Generate a reset token (simulate sending via email/SMS)
      String resetToken = userService.generateResetToken(username);

      // Return the token in the response for demonstration purposes
      return ResponseEntity.ok("Reset token (simulated): " + resetToken);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Error generating reset token: " + ex.getMessage());
    }
  }

  @PostMapping("/resetPassword")
  public ResponseEntity<?> resetPassword(
      @RequestParam String username,
      @RequestParam String resetToken,
      @RequestParam String newPassword) {
    try {
      // Reset the password using the provided token
      userService.resetPasswordWithToken(username, resetToken, newPassword);
      return ResponseEntity.ok("Password reset successfully (simulated).");
    } catch (IllegalArgumentException ex) {
      if (ex.getMessage().equals("Reset token has expired")) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Reset token is expired. Please request a new token.");
      }
      throw ex;
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error resetting password: " + ex.getMessage());
    }
  }





}


