package com.ase.bytealchemists.controller;

import com.ase.bytealchemists.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing service categories in the Homeless Support API.
 * This controller provides endpoints to handle operations related to service
 * categories,
 * such as adding, retrieving, and deleting service categories like shelters,
 * food banks,
 * healthcare centers, and more.
 *
 * @author Jason
 * @version 1.0
 */
@RestController
@RequestMapping("/services/categories")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  /**
   * End point for deleting a category by name.
   * This method attempts to delete a category with the specified name.
   * If the category is found and deleted successfully, a success message
   * is returned. If the category does not exist, an error message is returned.
   *
   * @param name the name of the category to be deleted.
   * @return a String message indicating the result of the operation.
   *         If successful, returns a success message. If the category is
   *         not found, returns an error message.
   */
  @DeleteMapping("/name/{name}")
  public String deleteCategoryByName(@PathVariable String name) {
    try {
      categoryService.deleteCategoryByName(name);
      return "Category with name '" + name + "' was deleted successfully.";
    } catch (IllegalArgumentException e) {
      return e.getMessage();
    }
  }
}
