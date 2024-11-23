package com.ase.bytealchemists.service;

import com.ase.bytealchemists.model.UserEntity;
import com.ase.bytealchemists.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Class UserService.
 *
 * @author Jason
 * @version 2024/11/21
 */
@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  /**
   * Registers a new user by saving their details to the database.
   *
   * @param userEntity The user details.
   * @return true if the user was successfully registered, false otherwise
   */
  public boolean registerUser(UserEntity userEntity) {
    // Check if username or email already exists
    Optional<UserEntity> existingUserByUsername =
        userRepository.findByUsername(userEntity.getUsername());
    Optional<UserEntity> existingUserByEmail = userRepository.findByEmail(userEntity.getEmail());

    if (existingUserByUsername.isPresent() || existingUserByEmail.isPresent()) {
      return false; // Conflict
    }

    // Encrypt password before saving
    userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
    userRepository.save(userEntity);
    return true; // Success
  }
  // Class body here

  /**
   * Verifies that the provided password matches the encoded password of the user.
   *
   * @param rawPassword the plain text password provided by the user during login
   * @param encodedPassword the encoded password stored in the database
   * @return true if the passwords match, false otherwise
   */
  public boolean verifyPassword(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }

  /**
   * Finds a user by their username.
   *
   * @param username the username to find
   * @return an {@link Optional} containing the {@link UserEntity} if found
   */
  public Optional<UserEntity> findUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

}

