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

  /**
   * Tests successfully resetting a user's password.
   */
  @Test
  public void testResetPassword_Success() {
    UserEntity mockUser = new UserEntity();
    mockUser.setUsername("johndoe");
    mockUser.setPassword("oldpassword");

    when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));
    when(passwordEncoder.encode("newSecurePassword")).thenReturn("encodedNewPassword");

    userService.resetPassword("johndoe", "newSecurePassword");

    verify(userRepository, times(1)).findByUsername("johndoe");
    verify(passwordEncoder, times(1)).encode("newSecurePassword");
    verify(userRepository, times(1)).save(mockUser);
    assertEquals("encodedNewPassword", mockUser.getPassword(), "Password should be updated.");
  }

  /**
   * Tests resetting a user's password when the user is not found.
   */
  @Test
  public void testResetPassword_UserNotFound() {
    when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

    try {
      userService.resetPassword("unknownuser", "newSecurePassword");
    } catch (IllegalArgumentException ex) {
      assertEquals("User not found", ex.getMessage(), "Exception message should "
          + "indicate user not found.");
    }

    verify(userRepository, times(1)).findByUsername("unknownuser");
    verify(passwordEncoder, times(0)).encode(any(String.class));
    verify(userRepository, times(0)).save(any(UserEntity.class));
  }

  /**
   * Tests verifying a password with null input.
   */
  @Test
  public void testVerifyPassword_NullInput() {
    boolean result = userService.verifyPassword(null, null);

    assertFalse(result, "Password verification should fail for null input.");
    verify(passwordEncoder, times(0)).matches(any(String.class), any(String.class));
  }

  /**
   * Tests finding a user by username with null input.
   */
  @Test
  public void testFindUserByUsername_NullUsername() {
    Optional<UserEntity> result = userService.findUserByUsername(null);

    assertFalse(result.isPresent(), "No user should be found for null username.");
    verify(userRepository, times(1)).findByUsername(null);
  }
}
