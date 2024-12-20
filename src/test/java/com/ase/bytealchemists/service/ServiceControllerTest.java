package com.ase.bytealchemists.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ase.bytealchemists.config.TestSecurityConfig;
import com.ase.bytealchemists.controller.ServiceController;
import com.ase.bytealchemists.model.ServiceEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;


/**
 * This class contains the unit tests for the ServiceController class.
 */
@Import(TestSecurityConfig.class)

@WebMvcTest(ServiceController.class)
public class ServiceControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ServiceService serviceService;

  @MockBean
  private CategoryService categoryService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  // Test for registerService() method when success
  @Test
  void testRegisterService_ValidInput_ShouldReturn201() throws Exception {
    ServiceEntity serviceEntity = new ServiceEntity(
        1L, "Test Shelter", "shelters",
        40.748817, -73.985428,
        "123 Main St", "New York", "NY", "10001",
        "123-456-7890",
        "9 AM - 5 PM", true);

    // Mocking service calls
    when(categoryService.categoryExists("shelters")).thenReturn(true);
    when(serviceService.registerService(any(ServiceEntity.class))).thenReturn(serviceEntity);

    mockMvc.perform(post("/services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(serviceEntity)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(serviceEntity.getId()))
        .andExpect(jsonPath("$.name").value(serviceEntity.getName()))
        .andExpect(jsonPath("$.category").value(serviceEntity.getCategory()))
        .andExpect(jsonPath("$.latitude").value(serviceEntity.getLatitude()))
        .andExpect(jsonPath("$.longitude").value(serviceEntity.getLongitude()))
        .andExpect(jsonPath("$.address").value(serviceEntity.getAddress()))
        .andExpect(jsonPath("$.city").value(serviceEntity.getCity()))
        .andExpect(jsonPath("$.state").value(serviceEntity.getState()))
        .andExpect(jsonPath("$.zipcode").value(serviceEntity.getZipcode()))
        .andExpect(jsonPath("$.contactNumber").value(serviceEntity.getContactNumber()))
        .andExpect(jsonPath("$.operationHour").value(serviceEntity.getOperationHour()))
        .andExpect(jsonPath("$.availability").value(serviceEntity.getAvailability()));

    // Verifying the service interactions
    verify(categoryService, times(1)).categoryExists("shelters");
    verify(serviceService, times(1)).registerService(any(ServiceEntity.class));
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

  @Test
  void testQueryServices_ValidInput_ShouldReturn200() throws Exception {
    // Arrange: mock the data and service response
    ServiceEntity service1 = new ServiceEntity(
        1L, "Shelter A", "shelters", 40.748817, -73.985428,
        "123 Main St", "New York", "NY", "10001",
        "123-456-7890", "9 AM - 5 PM", true);

    ServiceEntity service2 = new ServiceEntity(
        2L, "Shelter B", "shelters", 40.748817, -73.985428,
        "456 Another St", "New York", "NY", "10002",
        "987-654-3210", "10 AM - 6 PM", true);

    List<ServiceEntity> services = Arrays.asList(service1, service2);

    // Mock the service layer call
    when(serviceService.queryServices(40.748817, -73.985428, "shelters", true))
        .thenReturn(services);

    // Act & Assert: Perform the GET request and verify the response
    mockMvc.perform(get("/services/query")
            .param("latitude", "40.748817")
            .param("longitude", "-73.985428")
            .param("category", "shelters")
            .param("availability", "true")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Shelter A"))
        .andExpect(jsonPath("$[1].name").value("Shelter B"))
        .andExpect(jsonPath("$[0].category").value("shelters"))
        .andExpect(jsonPath("$[1].category").value("shelters"));
  }

  @Test
  void testQueryServices_NoFilters_ShouldReturnEmptyList() throws Exception {
    // Arrange: mock the service to return an empty list
    when(serviceService.queryServices(null, null, null, null))
        .thenReturn(Arrays.asList());

    // Act & Assert: Perform the GET request with no filters and verify the response
    mockMvc.perform(get("/services/query")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void testQueryServices_InvalidCategory_ShouldReturnEmptyList() throws Exception {
    // Arrange: mock the service to return an empty list for an invalid category
    when(serviceService.queryServices(40.748817, -73.985428, "invalid-category", true))
        .thenReturn(Arrays.asList());

    // Act & Assert: Perform the GET request and verify the response
    mockMvc.perform(get("/services/query")
            .param("latitude", "40.748817")
            .param("longitude", "-73.985428")
            .param("category", "invalid-category")
            .param("availability", "true")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  /**
   * Tests the PUT /services/{id} endpoint with a full update.
   * Expects a 200 OK status and the updated service entity.
   */
  @Test
  void testUpdateService_FullUpdate_ValidInput_ShouldReturn200() throws Exception {
    Long serviceId = 1L;
    ServiceEntity existingService = new ServiceEntity(
            serviceId, "Old Service Name", "shelters",
            40.7128, -74.0060,
            "123 Old St", "Old City", "NY", "10001",
            "123-456-7890", "9 AM - 5 PM", true
    );

    ServiceEntity updatedService = new ServiceEntity(
            serviceId, "Updated Service Name", "shelters",
            40.7128, -74.0060,
            "456 New St", "New City", "NY", "10002",
            "987-654-3210", "10 AM - 6 PM", false
    );

    // Mock the serviceService.updateService method to return the updated service
    when(serviceService.updateService(eq(serviceId),
            any(ServiceEntity.class))).thenReturn(updatedService);

    // Prepare the full update JSON data
    String fullUpdateJson = objectMapper.writeValueAsString(updatedService);

    mockMvc.perform(put("/services/{id}", serviceId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(fullUpdateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(serviceId))
            .andExpect(jsonPath("$.name").value("Updated Service Name"))
            .andExpect(jsonPath("$.address").value("456 New St"))
            .andExpect(jsonPath("$.zipcode").value("10002"))
            .andExpect(jsonPath("$.contactNumber").value("987-654-3210"))
            .andExpect(jsonPath("$.availability").value(false));
  }

  /**
   * Tests the PUT /services/{id} endpoint with a partial update.
   * Expects a 200 OK status and the updated service entity with only specified fields changed.
   */
  @Test
  void testUpdateService_PartialUpdate_ValidInput_ShouldReturn200() throws Exception {
    Long serviceId = 1L;
    ServiceEntity existingService = new ServiceEntity(
            serviceId, "Old Service Name", "shelters",
            40.7128, -74.0060,
            "123 Old St", "Old City", "NY", "10001",
            "123-456-7890", "9 AM - 5 PM", true
    );

    // Updated fields: name and city
    ServiceEntity partialUpdatedService = new ServiceEntity();
    partialUpdatedService.setName("Updated Service Name");
    partialUpdatedService.setCity("New City");

    ServiceEntity returnedUpdatedService = new ServiceEntity(
            serviceId, "Updated Service Name", "shelters",
            40.7128, -74.0060,
            "123 Old St", "New City", "NY", "10001",
            "123-456-7890", "9 AM - 5 PM", true
    );

    // Mock the serviceService.updateService method to return the updated service
    when(serviceService.updateService(eq(serviceId), any(ServiceEntity.class)))
            .thenReturn(returnedUpdatedService);

    // Prepare the partial update JSON data
    String partialUpdateJson = "{ \"name\": \"Updated Service Name\", \"city\": \"New City\" }";

    mockMvc.perform(put("/services/{id}", serviceId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(partialUpdateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(serviceId))
            .andExpect(jsonPath("$.name").value("Updated Service Name"))
            .andExpect(jsonPath("$.city").value("New City"))
            .andExpect(jsonPath("$.address").value("123 Old St")) // Unchanged field
            .andExpect(jsonPath("$.zipcode").value("10001")) // Unchanged field
            .andExpect(jsonPath("$.contactNumber").value("123-456-7890")) // Unchanged field
            .andExpect(jsonPath("$.availability").value(true)); // Unchanged field
  }

  /**
   * Tests the PUT /services/{id} endpoint when attempting to update a non-existing service.
   * Expects a 404 Not Found status.
   */
  @Test
  void testUpdateService_NonExistingId_ShouldReturn404() throws Exception {
    Long serviceId = 999L;
    ServiceEntity updateData = new ServiceEntity();
    updateData.setName("Non-Existent Service");

    // Mock the serviceService.updateService method to return null, indicating service not found
    when(serviceService.updateService(eq(serviceId), any(ServiceEntity.class))).thenReturn(null);

    // Prepare the update JSON data
    String updateJson = objectMapper.writeValueAsString(updateData);

    mockMvc.perform(put("/services/{id}", serviceId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateJson))
            .andExpect(status().isNotFound());
  }

  /**
   * Tests the DELETE /services/{id} endpoint for an existing service.
   * Expects a 204 No Content status.
   */
  @Test
  void testDeleteService_ExistingId_ShouldReturn204() throws Exception {
    Long serviceId = 1L;

    // Mock the serviceService.deleteServiceById method to return true, indicate successful deletion
    when(serviceService.deleteServiceById(serviceId)).thenReturn(true);

    mockMvc.perform(delete("/services/{id}", serviceId))
            .andExpect(status().isNoContent());
  }

  /**
   * Tests the DELETE /services/{id} endpoint when attempting to delete a non-existing service.
   * Expects a 404 Not Found status.
   */
  @Test
  void testDeleteService_NonExistingId_ShouldReturn404() throws Exception {
    Long serviceId = 999L;

    // Mock the serviceService.deleteServiceById method to return false, service not found
    when(serviceService.deleteServiceById(serviceId)).thenReturn(false);

    mockMvc.perform(delete("/services/{id}", serviceId))
            .andExpect(status().isNotFound());
  }

  @Test
  void testGetAllServices_ShouldReturnListOfServices() throws Exception {
    ServiceEntity service1 = new ServiceEntity(
        1L, "Shelter A", "shelters", 40.748817, -73.985428,
        "123 Main St", "New York", "NY", "10001",
        "123-456-7890", "9 AM - 5 PM", true);

    ServiceEntity service2 = new ServiceEntity(
        2L, "Shelter B", "shelters", 40.748817, -73.985428,
        "456 Another St", "New York", "NY", "10002",
        "987-654-3210", "10 AM - 6 PM", true);

    List<ServiceEntity> services = Arrays.asList(service1, service2);

    when(serviceService.getAllServices()).thenReturn(services);
    mockMvc.perform(get("/services")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("Shelter A"))
        .andExpect(jsonPath("$[1].name").value("Shelter B"));
  }

  @Test
  void testGetAllServices_EmptyList_ShouldReturn200() throws Exception {
    when(serviceService.getAllServices()).thenReturn(Arrays.asList());

    mockMvc.perform(get("/services")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void testGetServiceById_ValidId_ShouldReturnService() throws Exception {
    Long serviceId = 1L;
    ServiceEntity service = new ServiceEntity(
        serviceId, "Shelter A", "shelters", 40.748817, -73.985428,
        "123 Main St", "New York", "NY", "10001",
        "123-456-7890", "9 AM - 5 PM", true);

    when(serviceService.getServiceById(serviceId)).thenReturn(Optional.of(service));

    mockMvc.perform(get("/services/{id}", serviceId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(serviceId))
        .andExpect(jsonPath("$.name").value("Shelter A"))
        .andExpect(jsonPath("$.category").value("shelters"));
  }

  @Test
  void testGetServiceById_NonExistingId_ShouldReturn404() throws Exception {
    Long serviceId = 999L;
    when(serviceService.getServiceById(serviceId)).thenReturn(Optional.empty());

    mockMvc.perform(get("/services/{id}", serviceId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }


}
