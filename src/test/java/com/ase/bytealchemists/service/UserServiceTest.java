package com.ase.bytealchemists.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ase.bytealchemists.config.TestSecurityConfig;
import com.ase.bytealchemists.model.UserEntity;
import com.ase.bytealchemists.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Unit tests for UserService.
 */
@Import(TestSecurityConfig.class)

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private BCryptPasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this); // Initialize mocks
  }

  /**
   * Tests registering a new user successfully.
   */
  @Test
  public void testRegisterUser_Success() {
    // Arrange
    UserEntity mockUser = new UserEntity();
    mockUser.setUsername("johndoe");
    mockUser.setEmail("johndoe@example.com");
    mockUser.setPassword("securepassword");

    when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(mockUser.getPassword())).thenReturn("encryptedpassword");
    when(userRepository.save(any(UserEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    boolean result = userService.registerUser(mockUser);

    // Assert
    assertTrue(result, "User should be registered successfully.");
    verify(userRepository, times(1)).findByUsername(mockUser.getUsername());
    verify(userRepository, times(1)).findByEmail(mockUser.getEmail());
    verify(passwordEncoder, times(1)).encode("securepassword");
    verify(userRepository, times(1)).save(any(UserEntity.class));
  }

  /**
   * Tests the scenario where the password verification matches.
   */
  @Test
  public void testVerifyPassword_Match() {
    // Mock passwordEncoder.matches() method
    when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

    // Execute test
    boolean result = userService.verifyPassword("rawPassword", "encodedPassword");

    // Verify result
    assertTrue(result, "Password should match.");
    verify(passwordEncoder, times(1)).matches("rawPassword", "encodedPassword");
  }

  /**
   * Tests the scenario where the password verification does not match.
   */
  @Test
  public void testVerifyPassword_NotMatch() {
    // Mock passwordEncoder.matches() method
    when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(false);

    // Execute test
    boolean result = userService.verifyPassword("rawPassword", "encodedPassword");

    // Verify result
    assertFalse(result, "Password should not match.");
    verify(passwordEncoder, times(1)).matches("rawPassword", "encodedPassword");
  }

  /**
   * Tests successfully finding a user by username.
   */
  @Test
  public void testFindUserByUsername_Success() {
    // Prepare data
    UserEntity mockUser = new UserEntity();
    mockUser.setUsername("johndoe");

    when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));

    // Execute test
    Optional<UserEntity> result = userService.findUserByUsername("johndoe");

    // Verify result
    assertTrue(result.isPresent(), "User should be found.");
    assertEquals("johndoe", result.get().getUsername());
    verify(userRepository, times(1)).findByUsername("johndoe");
  }

  /**
   * Tests the scenario where finding a user by username fails (user not found).
   */
  @Test
  public void testFindUserByUsername_NotFound() {
    when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

    // Execute test
    Optional<UserEntity> result = userService.findUserByUsername("unknownuser");

    // Verify result
    assertFalse(result.isPresent(), "User should not be found.");
    verify(userRepository, times(1)).findByUsername("unknownuser");
  }

  @Test
  public void testGenerateResetToken_Success() {
    UserEntity mockUser = new UserEntity();
    mockUser.setUsername("johndoe");
    mockUser.setEmail("johndoe@example.com");
    mockUser.setResetToken(null);

    when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));
    when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

    String resetToken = userService.generateResetToken("johndoe");

    assertTrue(resetToken.length() > 0, "Reset token should be generated.");
    assertEquals(resetToken, mockUser.getResetToken(), "Reset token should be saved in user entity.");
    verify(userRepository, times(1)).findByUsername("johndoe");
    verify(userRepository, times(1)).save(any(UserEntity.class));
  }

  @Test
  public void testGenerateResetToken_UserNotFound() {
    when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

    IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> userService.generateResetToken("unknownuser"),
        "Expected generateResetToken to throw, but it didn't."
    );

    assertEquals("User not found", exception.getMessage());
    verify(userRepository, times(1)).findByUsername("unknownuser");
  }

  @Test
  public void testResetPasswordWithToken_Success() {
    UserEntity mockUser = new UserEntity();
    mockUser.setUsername("johndoe");
    mockUser.setPassword("oldPassword");
    mockUser.setResetToken("valid-token");
    mockUser.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));

    when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));
    when(passwordEncoder.encode("NewPassword@123")).thenReturn("encodedNewPassword");
    when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

    userService.resetPasswordWithToken("johndoe", "valid-token", "NewPassword@123");

    assertEquals("encodedNewPassword", mockUser.getPassword(), "Password should be updated.");
    assertNull(mockUser.getResetToken(), "Reset token should be cleared.");
    assertNull(mockUser.getResetTokenExpiry(), "Reset token expiry should be cleared.");
    verify(userRepository, times(1)).findByUsername("johndoe");
    verify(passwordEncoder, times(1)).encode("NewPassword@123");
    verify(userRepository, times(1)).save(mockUser);
  }

  @Test
  public void testResetPasswordWithToken_InvalidToken() {
    UserEntity mockUser = new UserEntity();
    mockUser.setUsername("johndoe");
    mockUser.setResetToken("valid-token");

    when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));

    IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> userService.resetPasswordWithToken("johndoe", "invalid-token", "NewPassword@123"),
        "Expected resetPasswordWithToken to throw, but it didn't."
    );

    assertEquals("Invalid reset token", exception.getMessage());
    verify(userRepository, times(1)).findByUsername("johndoe");
  }

  @Test
  public void testResetPasswordWithToken_ExpiredToken() {
    UserEntity mockUser = new UserEntity();
    mockUser.setUsername("johndoe");
    mockUser.setResetToken("valid-token");
    mockUser.setResetTokenExpiry(LocalDateTime.now().minusMinutes(1)); // Token expired

    when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));

    IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> userService.resetPasswordWithToken("johndoe", "valid-token", "NewPassword@123"),
        "Expected resetPasswordWithToken to throw, but it didn't."
    );

    assertEquals("Reset token has expired", exception.getMessage());
    verify(userRepository, times(1)).findByUsername("johndoe");
  }

  @Test
  public void testResetPasswordWithToken_InvalidPassword() {
    UserEntity mockUser = new UserEntity();
    mockUser.setUsername("johndoe");
    mockUser.setResetToken("valid-token");
    mockUser.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));

    when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));

    IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> userService.resetPasswordWithToken("johndoe", "valid-token", "short"),
        "Expected resetPasswordWithToken to throw, but it didn't."
    );

    assertEquals("Password must be at least 8 characters long", exception.getMessage());
    verify(userRepository, times(1)).findByUsername("johndoe");
  }








}
