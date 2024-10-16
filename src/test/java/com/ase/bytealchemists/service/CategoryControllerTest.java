package com.ase.bytealchemists.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ase.bytealchemists.controller.CategoryController;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * This class contains the unit tests for the CategoryController class.
 */
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
    when(categoryService.addCategoryByName("Shelter")).thenReturn(true);

    // Perform POST request and verify the response
    mockMvc.perform(post("/services/categories/name/Shelter")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Attribute was updated successfully."));

    verify(categoryService, times(1)).addCategoryByName("Shelter");
  }

  // Test for addCategoryByName() method when category cannot be added.
  @Test
  public void testAddCategoryByName_CategoryAlreadyExists() throws Exception {
    when(categoryService.addCategoryByName("Shelter")).thenReturn(false);

    mockMvc.perform(post("/services/categories/name/Shelter")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(content().string("Category already exists."));

    verify(categoryService, times(1)).addCategoryByName("Shelter");
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
}

