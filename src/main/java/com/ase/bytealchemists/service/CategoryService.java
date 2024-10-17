package com.ase.bytealchemists.service;

import com.ase.bytealchemists.model.CategoryEntity;
import com.ase.bytealchemists.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.TransactionException;

/**
 * Service class for managing operations related to service categories in the
 * Homeless Support API.
 * This class provides the business logic for handling categories, such as
 * adding, updating,
 * retrieving, and deleting categories. It acts as a bridge between the
 * controller layer and the
 * repository layer, ensuring that the necessary business logic is applied
 * before interacting
 * with the data stored in the database.
 *
 * @author Jason
 * @version 1.0
 */
@Service
public class CategoryService {
  @Autowired
  private CategoryRepository categoryRepository;

  /**
   * Adds a category to the database by its name if it doesn't exist.
   * This method checks if a category with the specified name is present
   * in the database first. If found, then it cannot be added and return false;
   * else add the category and return true.
   *
   * @param categoryName the name of the category to delete.
   * @return true if a category is added successfully,
   *         false if the category already exists.
   */
  @Transactional
  public boolean addCategoryByName(String categoryName) {
    if (categoryRepository.existsByCategoryName(categoryName)) {
      return false; // category already exists
    }
    CategoryEntity category = new CategoryEntity();
    category.setCategoryName(categoryName);

    // save the new category
    categoryRepository.save(category);
    return true; // category added successfully

  }

  /**
   * Deletes a category from the database by its name if it exists.
   * This method checks if a category with the specified name is present
   * in the database. If found, the category is deleted. If the category
   * does not exist, no action is taken.
   *
   * @param categoryName the name of the category to delete.
   * @throws TransactionException if there are issues with transaction
   *                              management during deletion.
   */
  @Transactional
  public void deleteCategoryByName(String categoryName) {
    Optional<CategoryEntity> category = categoryRepository.findByCategoryName(categoryName);
    category.ifPresent(categoryRepository::delete);
  }

  /**
   * Checks whether a category with the specified name exists in the database.
   * This method queries the "category" table to determine if a category with
   * the given name is present.
   *
   * @param categoryName the name of the category to search for.
   * @return true if a category with the specified name exists,
   *         false otherwise.
   */
  public boolean categoryExists(String categoryName) {
    return categoryRepository.existsByCategoryName(categoryName);
  }

  /**
   * Retrieve a list of categories related to services
   * This method queries the "category" table to find all the categoryName
   * in the table.
   *
   * @return a list of services that match the given filters
   */
  public List<String> getAllCategories() {
    return categoryRepository.findAllCategoryNames();
  }
}
