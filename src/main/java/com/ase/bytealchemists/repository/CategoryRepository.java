package com.ase.bytealchemists.repository;

import com.ase.bytealchemists.model.CategoryEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Category entities in the database.
 * This interface provides methods for interacting with the "category" table in
 * the database,
 * including basic CRUD operations. It is a part of the persistence layer and
 * uses Spring Data JPA.
 *
 * @author Jason
 * @version 1.0
 */
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

  /**
   * Checks if a category with the specified name exists in the database.
   * This method queries the "category" table to determine whether a category
   * with the given name is present.
   *
   * @param categoryName the name of the category to search for.
   * @return true if a category with the specified name exists,
   *         false} otherwise.
   */
  boolean existsByCategoryName(String categoryName);

  /**
   * Retrieves a category entity by its name.
   * This method queries the "category" table to fetch a category that matches
   * the given name. If a matching category is found, it is returned inside
   * an Optional. If no match is found, the Optional will be empty.
   *
   * @param categoryName the name of the category to retrieve.
   * @return an Optional containing the category entity if found,
   *         or an empty Optional if not found.
   */
  Optional<CategoryEntity> findByCategoryName(String categoryName);

  /**
   * Deletes a category from the database by its name.
   * This method removes the category with the specified name from the
   * "category" table. If no category with the given name exists,
   * no operation is performed.
   *
   * @param categoryName the name of the category to delete.
   */
  void deleteByCategoryName(String categoryName);
}
