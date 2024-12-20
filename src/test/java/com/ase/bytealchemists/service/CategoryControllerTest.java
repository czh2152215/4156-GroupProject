package com.ase.bytealchemists.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ase.bytealchemists.config.TestSecurityConfig;
import com.ase.bytealchemists.controller.CategoryController;
import com.ase.bytealchemists.model.CategoryEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * This class contains the unit tests for the CategoryController class.
 */
@Import(TestSecurityConfig.class)

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CategoryService categoryService;

  // Test for getAllCategories() method when there are categories.
  @Test
  public void testGetAllCategoriesWhenCategoriesExist() throws Exception {
    List<String> mockCategories = Arrays.asList("Food Bank", "Shelter", "Healthcare Center");
    when(categoryService.getAllCategories()).thenReturn(mockCategories);

    mockMvc.perform(get("/services/categories")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Food Bank, Shelter, Healthcare Center"));

    verify(categoryService, times(1)).getAllCategories();
  }

  // Test for getAllCategories() method when there is no category.
  @Test
  public void testGetAllCategoriesWhenCategoryNotExist() throws Exception {
    when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/services/categories")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(categoryService, times(1)).getAllCategories();
  }

  // Test for getAllCategories() method when there is exception.
  @Test
  public void testGetAllCategoriesWhenExceptionExists() throws Exception {
    when(categoryService.getAllCategories())
        .thenThrow(new RuntimeException("Database connection error"));

    mockMvc.perform(get("/services/categories")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("An Error has occurred."));

    verify(categoryService, times(1)).getAllCategories();
  }

  // Test for addCategoryByName() method when category is added successfully.
  @Test
  public void testAddCategoryByName_Success() throws Exception {
    String categoryName = "Shelters";
    CategoryEntity mockCategory = new CategoryEntity();
    mockCategory.setId(1L);
    mockCategory.setCategoryName(categoryName);

    when(categoryService.addCategoryByName(categoryName)).thenReturn(Optional.of(mockCategory));

    // Act & Assert
    mockMvc.perform(post("/services/categories/name/{name}", categoryName)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.categoryName").value(categoryName));

    verify(categoryService, times(1)).addCategoryByName(categoryName);
  }

  // Test for addCategoryByName() method when category cannot be added.
  @Test
  public void testAddCategoryByName_CategoryAlreadyExists() throws Exception {
    String categoryName = "Shelters";

    when(categoryService.addCategoryByName(categoryName)).thenReturn(Optional.empty());

    mockMvc.perform(post("/services/categories/name/{name}", categoryName)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(content().string("Category already exists."));

    verify(categoryService, times(1)).addCategoryByName(categoryName);
  }

  // Test for addCategoryByName() method when the input category name is a blank string.
  @Test
  public void testAddCategoryByNameWhenInputBlank() throws Exception {
    mockMvc.perform(post("/services/categories/name/ ")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid category name."));

    // Ensure no service call was made for invalid name
    verify(categoryService, times(0)).addCategoryByName("Shelter");
  }

  // Test for addCategoryByName() method when the input category name is an empty string.
  @Test
  public void testAddCategoryByNameWhenInputEmpty() throws Exception {
    mockMvc.perform(post("/services/categories/name/")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    // Ensure no service call was made for invalid name
    verify(categoryService, times(0)).addCategoryByName("Shelter");
  }

  // Test for addCategoryByName() method when there is exception.
  @Test
  public void testAddCategoryByNameWhenExceptionExists() throws Exception {
    when(categoryService.addCategoryByName("Shelter"))
        .thenThrow(new RuntimeException("Database connection error"));

    mockMvc.perform(post("/services/categories/name/Shelter")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("An Error has occurred."));

    verify(categoryService, times(1)).addCategoryByName("Shelter");
  }

  // Test for deleteCategoryByName() method when success
  @Test
  public void testDeleteCategoryByNameWhenSuccess() throws Exception {
    String categoryName = "Shelter";

    when(categoryService.categoryExists("Shelter")).thenReturn(true);

    mockMvc.perform(delete("/services/categories/name/{name}", categoryName)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Category with name 'Shelter' was deleted successfully."));

    // Verify service interaction
    verify(categoryService, times(1)).deleteCategoryByName(categoryName);
  }

  // Test for deleteCategoryByName() method when category does not exist
  @Test
  public void testDeleteCategoryByNameWhenFail() throws Exception {
    String categoryName = "Shelter";

    when(categoryService.categoryExists("Shelter")).thenReturn(false);

    mockMvc.perform(delete("/services/categories/name/{name}", categoryName)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Category with name 'Shelter' does not exist."));

    // Verify service interaction
    verify(categoryService, times(0)).deleteCategoryByName(categoryName);
  }

  // Test for deleteCategoryByName() method when there is exception
  @Test
  public void testDeleteCategoryByNameWhenExceptionExists() throws Exception {
    String categoryName = "Shelter";

    when(categoryService.categoryExists("Shelter"))
        .thenThrow(new RuntimeException("Database connection error"));

    mockMvc.perform(delete("/services/categories/name/{name}", categoryName)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("An error occurred while deleting the category."));

    // Verify service interaction
    verify(categoryService, times(0)).deleteCategoryByName(categoryName);
  }
}