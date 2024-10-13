package com.ase.bytealchemists.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ase.bytealchemists.controller.ServiceController;
import com.ase.bytealchemists.model.ServiceEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * This class contains the unit tests for the ServiceController class.
 */
@WebMvcTest(ServiceController.class)
public class ServiceControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ServiceService serviceService;

  @MockBean
  private CategoryService categoryService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testRegisterService_ValidInput_ShouldReturn201() throws Exception {
    ServiceEntity serviceEntity = new ServiceEntity(
        null, "Test Shelter", "shelters",
        40.748817, -73.985428,
        "123 Main St", "New York", "NY", "10001",
        "123-456-7890",
        "9 AM - 5 PM", true);

    when(categoryService.categoryExists("shelters")).thenReturn(true);
    when(serviceService.registerService(any(ServiceEntity.class))).thenReturn(serviceEntity);

    mockMvc.perform(post("/services")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(serviceEntity)))
        .andExpect(status().isCreated())
        .andExpect(content().string("Service registered successfully"));
  }

  @Test
  void testRegisterService_InvalidInput_ShouldReturn400() throws Exception {
    // Arrange
    ServiceEntity invalidService = new ServiceEntity(
        null, "",
        "shelters",
        95.0, -73.985428, // Invalid name and latitude
        "", "New York", "NY", "ABCDE", // Invalid address and zipcode
        "123-456", // Invalid contact number
        "9 AM - 5 PM", true);

    mockMvc.perform(post("/services")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidService)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name").value("Service name cannot be blank"))
        .andExpect(jsonPath("$.latitude").value("Latitude must be <= 90"))
        .andExpect(jsonPath("$.address").value("Address cannot be blank"))
        .andExpect(jsonPath("$.zipcode").value("Zipcode must be a 5-digit number"));
  }

  @Test
  void testRegisterService_CategoryDoesNotExist_ShouldReturn400() throws Exception {
    // Arrange
    ServiceEntity serviceEntity = new ServiceEntity(
        null, "Shelter", "invalid-category", 40.748817, -73.985428,
        "123 Main St", "New York", "NY", "10001",
        "123-456-7890", "9 AM - 5 PM", true);

    when(categoryService.categoryExists("invalid-category")).thenReturn(false);

    // Act & Assert
    mockMvc.perform(post("/services")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(serviceEntity)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Category does not exist"));
  }
}
