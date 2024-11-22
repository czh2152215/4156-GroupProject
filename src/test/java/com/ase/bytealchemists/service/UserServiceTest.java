package com.ase.bytealchemists.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ase.bytealchemists.model.UserEntity;
import com.ase.bytealchemists.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Unit tests for UserService.
 */
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
}
