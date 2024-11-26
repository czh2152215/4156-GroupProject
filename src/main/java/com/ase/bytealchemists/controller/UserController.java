package com.ase.bytealchemists.controller;

import com.ase.bytealchemists.model.UserEntity;
import com.ase.bytealchemists.service.UserService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
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
        return new ResponseEntity<>(userService.findUserByUsername(userEntity.getUsername()), HttpStatus.CREATED);
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
    if (loginRequest.getUsername() == null || loginRequest.getUsername().isBlank()) {
      return ResponseEntity.badRequest().body("Username cannot be blank");
    }
    if (loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
      return ResponseEntity.badRequest().body("Password cannot be blank");
    }

    try {
      // check exists
      Optional<UserEntity> userOptional = userService
              .findUserByUsername(loginRequest.getUsername());
      if (userOptional.isPresent()) {
        UserEntity user = userOptional.get();

        // password
        boolean passwordMatches = userService.verifyPassword(loginRequest.getPassword(),
                user.getPassword());
        if (passwordMatches) {
          // map struct
          Map<String, Object> response = new HashMap<>();
          response.put("userId", user.getId());
          response.put("message", "Login successful");
          return ResponseEntity.ok(response);
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

  /**
   * Endpoint for resetting the user's password.
   *
   * <p>This method allows the user to reset their password by providing their username
   * and a new password. The password is validated and updated in the database.
   *
   * @param username The username of the user requesting the password reset.
   * @param newPassword The new password to set for the user.
   * @return A {@link ResponseEntity} indicating the result of the operation:
   */
  @PostMapping("/resetPassword")
  public ResponseEntity<?> resetPassword(
      @RequestParam String username,
      @RequestParam String newPassword) {
    if (username.isBlank()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username cannot be blank");
    }
    if (newPassword.isBlank()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password cannot be blank");
    }

    try {
      // Reset the password
      userService.resetPassword(username, newPassword);
      return ResponseEntity.ok("Password updated successfully.");
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
  }
}


