package com.ase.bytealchemists.repository;

import com.ase.bytealchemists.model.ServiceEntity;
import com.ase.bytealchemists.model.UserEntity;
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
}
