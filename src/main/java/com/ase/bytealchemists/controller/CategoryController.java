package com.ase.bytealchemists.controller;

import com.ase.bytealchemists.service.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
   * End point for adding a category by name.
   * This method attempts to add a category with the specified name.
   *
   * @param name the name of the category to be added.
   * @return A {@code ResponseEntity} object containing an HTTP 200 response
   *         with an appropriate message or the proper status code in tune
   *         with what has happened.
   */
  @PostMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> addCategoryByName(@PathVariable String name) {
    try {
      if (name.trim().isEmpty()) {
        return new ResponseEntity<>("Invalid category name.", HttpStatus.BAD_REQUEST);
      }
      if (categoryService.addCategoryByName(name)) {
        return new ResponseEntity<>("Attribute was updated successfully.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Category already exists.", HttpStatus.CONFLICT);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * * End point for deleting a category by name.
   * This method attempts to delete a category with the specified name.
   * If the category is found and deleted successfully, a success message
   * is returned. If the category does not exist, an error message is returned.
   *
   * @param name the name of the category to be deleted.
   * @return a ResponseEntity with a message indicating the result of the
   *         operation.
   *         If successful, returns a success message with HTTP status 200 (OK).
   *         If the category is not found, returns an error message with HTTP
   *         status 404 (Not Found).
   */
  @DeleteMapping("/name/{name}")
  public ResponseEntity<String> deleteCategoryByName(@PathVariable String name) {
    try {
      if (!categoryService.categoryExists(name)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Category with name '" + name + "' does not exist.");
      }
      categoryService.deleteCategoryByName(name);
      return ResponseEntity.ok("Category with name '" + name + "' was deleted successfully.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while deleting the category.");
    }
  }


  /**
   * End point for retrieving all category names related to services.
   *
   * @return A {@code ResponseEntity} object containing all the category names
   *         related to services
   *         and an HTTP 200 response, or an appropriate message indicating the
   *         proper response.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getAllCategories() {
    try {
      List<String> categories = categoryService.getAllCategories();
      if (categories.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } else {
        return new ResponseEntity<>(String.join(", ", categories), HttpStatus.OK);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  public ResponseEntity<?> handleException(Exception e) {
    System.out.println(e.toString());
    return new ResponseEntity<>("An Error has occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}