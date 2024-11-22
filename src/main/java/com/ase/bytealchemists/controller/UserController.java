package com.ase.bytealchemists.controller;

import com.ase.bytealchemists.model.UserEntity;
import com.ase.bytealchemists.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}

