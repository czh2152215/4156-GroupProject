package com.ase.bytealchemists.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class UserEntity.
 *
 * @author Jason
 * @version 2024/11/21
 */
@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class UserEntity {
  // Class body here
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Username cannot be blank")
  @Column(nullable = false, unique = true)
  private String username;

  @NotBlank(message = "First name cannot be blank")
  @Column(nullable = false)
  private String firstName;

  @NotBlank(message = "Last name cannot be blank")
  @Column(nullable = false)
  private String lastName;

  @NotBlank(message = "Password cannot be blank")
  @Column(nullable = false)
  private String password;

  @NotBlank(message = "Email cannot be blank")
  @Email(message = "Invalid email format")
  @Column(nullable = false, unique = true)
  private String email;

  @Pattern(regexp = "\\+?[0-9]{10,15}",
      message = "Phone number must be 10-15 digits, optionally starting with +")
  private String phone;
}

