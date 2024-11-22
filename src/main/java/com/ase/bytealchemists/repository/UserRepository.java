package com.ase.bytealchemists.repository;

import com.ase.bytealchemists.model.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing User entities in the database.
 * This interface extends {@link JpaRepository}
 * to provide CRUD operations for {@link UserEntity}.
 *
 * @author Jason
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  /**
   * Retrieves a user entity by its username.
   *
   * <p>This method returns an {@link Optional} containing the {@link UserEntity} if a user with the
   * specified username exists, or an empty {@link Optional} if no such user is found.
   *
   * @param username the username of the user to retrieve
   * @return an {@link Optional} containing the {@link UserEntity} if found
   * @throws jakarta.validation.ConstraintViolationException if the username is blank
   */
  Optional<UserEntity> findByUsername(
      @NotBlank(message = "Username cannot be blank") String username);

  /**
   * Retrieves a user entity by its email.
   *
   * <p>This method returns an {@link Optional} containing the {@link UserEntity}
   * if a user with the
   * specified email exists, or an empty {@link Optional} if no such user is found.
   *
   * @param email the email of the user to retrieve
   * @return an {@link Optional} containing the {@link UserEntity} if found
   * @throws jakarta.validation.ConstraintViolationException if the email is blank or invalid
   */
  Optional<UserEntity> findByEmail(@NotBlank(message = "Email cannot be blank")
                                   @Email(message = "Invalid email format") String email);
}
