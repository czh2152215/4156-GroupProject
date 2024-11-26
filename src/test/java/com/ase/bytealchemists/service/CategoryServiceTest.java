package com.ase.bytealchemists.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ase.bytealchemists.config.TestSecurityConfig;
import com.ase.bytealchemists.model.CategoryEntity;
import com.ase.bytealchemists.repository.CategoryRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;


/**
 * This class contains the unit tests for the CategoryService class.
 */
@Import(TestSecurityConfig.class)
public class CategoryServiceTest {

  @Mock
  private CategoryRepository categoryRepository;  // Mock the CategoryRepository

  @InjectMocks
  private CategoryService categoryService;  // Inject the mock repository into the service

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);  // Initialize the mocks
  }

  // Test for getAllCategories() method when there are categories.
  @Test
  public void testGetAllCategoriesWhenCategoriesExist() {
    List<String> mockCategories = Arrays.asList("Food Bank", "Shelter", "Healthcare Center");
    when(categoryRepository.findAllCategoryNames()).thenReturn(mockCategories);

    List<String> categories = categoryService.getAllCategories();

    assertNotNull(categories);
    assertEquals(3, categories.size());
    assertEquals("Food Bank", categories.get(0));
    assertEquals("Shelter", categories.get(1));
    assertEquals("Healthcare Center", categories.get(2));
    verify(categoryRepository, times(1)).findAllCategoryNames();
  }

  // Test for getAllCategories method when there is no category.
  @Test
  public void testGetAllCategoriesWhenCategoryNotExist() {
    when(categoryRepository.findAllCategoryNames()).thenReturn(Collections.emptyList());

    List<String> categories = categoryService.getAllCategories();

    assertNotNull(categories);
    assertTrue(categories.isEmpty());
    verify(categoryRepository, times(1)).findAllCategoryNames();
  }

  // Test for addCategoryByName method when category is added successfully.
  @Test
  public void testAddCategoryByNameCategoryWhenSuccess() {
    String categoryName = "Shelters";
    CategoryEntity mockCategory = new CategoryEntity();
    mockCategory.setId(1L);
    mockCategory.setCategoryName(categoryName);

    when(categoryRepository.existsByCategoryName(categoryName)).thenReturn(false);
    when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(mockCategory);

    Optional<CategoryEntity> result = categoryService.addCategoryByName(categoryName);

    assertTrue(result.isPresent());
    assertEquals(categoryName, result.get().getCategoryName());
    verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
  }

  // Test for addCategoryByName method when category cannot be added.
  @Test
  public void testAddCategoryByNameCategoryWhenFail() {
    String categoryName = "Shelters";

    when(categoryRepository.existsByCategoryName(categoryName)).thenReturn(true);

    Optional<CategoryEntity> result = categoryService.addCategoryByName(categoryName);

    assertFalse(result.isPresent());
    verify(categoryRepository, never()).save(any(CategoryEntity.class));
  }

  // Test for deleteCategoryByName() method when category is deleted successfully
  @Test
  void testDeleteCategoryByNameWhenCategoryExists() {
    String categoryName = "Shelter";
    CategoryEntity mockCategory = new CategoryEntity();
    mockCategory.setCategoryName(categoryName);

    when(categoryRepository.findByCategoryName(categoryName)).thenReturn(Optional.of(mockCategory));
    doNothing().when(categoryRepository).delete(mockCategory);

    categoryService.deleteCategoryByName(categoryName);

    verify(categoryRepository, times(1)).findByCategoryName(categoryName);
    verify(categoryRepository, times(1)).delete(mockCategory);
  }

  // Test for deleteCategoryByName() method when category exists
  @Test
  void testDeleteCategoryByNameWhenCategoryDoesNotExist() {
    String categoryName = "Shelter";

    when(categoryRepository.findByCategoryName(categoryName)).thenReturn(Optional.empty());

    categoryService.deleteCategoryByName(categoryName);

    verify(categoryRepository, times(1)).findByCategoryName(categoryName);
    verify(categoryRepository, never()).delete(any(CategoryEntity.class));
  }
}