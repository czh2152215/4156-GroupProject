package com.ase.bytealchemists.service;

import com.ase.bytealchemists.model.UserEntity;
import com.ase.bytealchemists.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
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

  /**
   * Generate a reset token for a user.
   *
   * @param username the username of the user
   * @return the generated reset token
   */
  public String generateResetToken(String username) {
    Optional<UserEntity> userOptional = userRepository.findByUsername(username);
    if (userOptional.isEmpty()) {
      throw new IllegalArgumentException("User not found");
    }

    UserEntity user = userOptional.get();

    // Generate a random token (UUID for simplicity)
    String resetToken = UUID.randomUUID().toString();

    // Simulate saving the token
    user.setResetToken(resetToken);
    user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30)); // Valid for 30 minutes
    userRepository.save(user);

    return resetToken;
  }

  /**
   * Reset the password using the provided token.
   *
   * @param username    the username of the user
   * @param resetToken  the reset token provided by the user
   * @param newPassword the new password to set
   */
  public void resetPasswordWithToken(String username, String resetToken, String newPassword) {
    Optional<UserEntity> userOptional = userRepository.findByUsername(username);
    if (userOptional.isEmpty()) {
      throw new IllegalArgumentException("User not found");
    }

    UserEntity user = userOptional.get();

    // Validate the reset token
    if (!resetToken.equals(user.getResetToken())) {
      throw new IllegalArgumentException("Invalid reset token");
    }

    // Validate token expiry
    if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Reset token has expired");
    }

    // Update the password
    validatePassword(newPassword);
    user.setPassword(passwordEncoder.encode(newPassword));
    user.setResetToken(null); // Clear the token
    user.setResetTokenExpiry(null);
    userRepository.save(user);
  }

  private void validatePassword(String password) {
    if (password.length() < 8) {
      throw new IllegalArgumentException("Password must be at least 8 characters long");
    }
    if (!password.matches(".*[A-Z].*")) {
      throw new IllegalArgumentException("Password must contain at least one uppercase letter");
    }
    if (!password.matches(".*[a-z].*")) {
      throw new IllegalArgumentException("Password must contain at least one lowercase letter");
    }
    if (!password.matches(".*\\d.*")) {
      throw new IllegalArgumentException("Password must contain at least one digit");
    }
    if (!password.matches(".*[@#$%^&+=].*")) {
      throw new IllegalArgumentException("Password must contain at least one special character (@#$%^&+=)");
    }
  }



}

