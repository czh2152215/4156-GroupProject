package com.ase.bytealchemists.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ase.bytealchemists.model.ServiceEntity;
import com.ase.bytealchemists.repository.ServiceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * This class contains the unit tests for the ServiceService class.
 */
public class ServiceServiceTest {

  @Mock
  private ServiceRepository serviceRepository;

  @InjectMocks
  private ServiceService serviceService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this); // Initialize mocks
  }

  @Test
  public void testQueryServices_withAllFilters() {
    // Prepare mock data
    List<ServiceEntity> mockServices = new ArrayList<>();
    mockServices.add(new ServiceEntity(1L, "Shelter A", "Shelter", 40.7128, -74.0060,
        "123 Main St", "New York", "NY", "10001", "1234567890", "9 AM - 5 PM", true));

    // Define the mock repository behavior
    when(serviceRepository.findByFilters(40.7128, -74.0060, 10.0, "Shelter", true))
        .thenReturn(mockServices);

    // Call the service method
    List<ServiceEntity> result = serviceService.queryServices(40.7128, -74.0060, "Shelter", true);

    // Verify the result
    assertEquals(1, result.size(), "There should be one service returned");
    assertEquals("Shelter A", result.get(0).getName(), "The service name should be 'Shelter A'");
  }

  @Test
  public void testQueryServices_withNoFilters() {
    // Prepare mock data
    List<ServiceEntity> mockServices = new ArrayList<>();
    mockServices.add(new ServiceEntity(2L, "Food Bank A", "Food Bank", 40.7306, -73.9352,
        "456 Broadway", "New York", "NY", "10002", "9876543210", "10 AM - 6 PM", true));

    // Define the mock repository behavior
    when(serviceRepository.findByFilters(40.7306, -73.9352, 10.0, null, null))
        .thenReturn(mockServices);

    // Call the service method without category and availability filters
    List<ServiceEntity> result = serviceService.queryServices(40.7306, -73.9352, null, null);

    // Verify the result
    assertEquals(1, result.size(), "There should be one service returned");
    assertEquals("Food Bank A", result.get(0).getName(),
        "The service name should be 'Food Bank A'");
  }

  @Test
  public void testQueryServices_withNoResults() {
    // Prepare mock data
    List<ServiceEntity> mockServices = new ArrayList<>();

    // Define the mock repository behavior
    when(serviceRepository.findByFilters(40.748817, -73.985428, 10.0, "Shelter", false))
        .thenReturn(mockServices);

    // Call the service method with no matching results
    List<ServiceEntity> result =
        serviceService.queryServices(40.748817, -73.985428, "Shelter", false);

    // Verify the result
    assertEquals(0, result.size(), "There should be no services returned");
  }

  /**
   * Tests the updateService method with a full update.
   * Expects all fields to be updated and the updated service to be returned.
   */
  @Test
  public void testUpdateService_FullUpdate_ShouldReturnUpdatedService() {
    // Arrange
    Long serviceId = 1L;
    ServiceEntity existingService = new ServiceEntity(
            serviceId, "Old Service Name", "shelters",
            40.7128, -74.0060,
            "123 Old St", "Old City", "NY", "10001",
            "123-456-7890", "9 AM - 5 PM", true
    );

    ServiceEntity updateData = new ServiceEntity(
            serviceId, "Updated Service Name", "shelters",
            40.7128, -74.0060,
            "456 New St", "New City", "NY", "10002",
            "987-654-3210", "10 AM - 6 PM", false
    );

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(existingService));
    when(serviceRepository.save(any(ServiceEntity.class))).thenReturn(updateData);

    // Act
    ServiceEntity result = serviceService.updateService(serviceId, updateData);

    // Assert
    assertNotNull(result, "The updated service should not be null");
    assertEquals("Updated Service Name", result.getName(), "Service name should be updated");
    assertEquals("456 New St", result.getAddress(), "Service address should be updated");
    assertEquals("New City", result.getCity(), "Service city should be updated");
    assertEquals("10002", result.getZipcode(), "Service zipcode should be updated");
    assertEquals("987-654-3210", result.getContactNumber(),
            "Service contact number should be updated");
    assertFalse(result.getAvailability(), "Service availability should be updated to false");
    verify(serviceRepository, times(1)).findById(serviceId);
    verify(serviceRepository, times(1)).save(existingService);
  }

  /**
   * Tests the updateService method with a partial update.
   * Expects only specified fields to be updated and others to remain unchanged.
   */
  @Test
  public void testUpdateService_PartialUpdate_ShouldReturnUpdatedService() {
    // Arrange
    Long serviceId = 1L;
    ServiceEntity existingService = new ServiceEntity(
            serviceId, "Old Service Name", "shelters",
            40.7128, -74.0060,
            "123 Old St", "Old City", "NY", "10001",
            "123-456-7890", "9 AM - 5 PM", true
    );

    ServiceEntity updateData = new ServiceEntity();
    updateData.setName("Partial Updated Service Name");
    updateData.setCity("Partial New City");

    ServiceEntity updatedService = new ServiceEntity(
            serviceId, "Partial Updated Service Name", "shelters",
            40.7128, -74.0060,
            "123 Old St", "Partial New City", "NY", "10001",
            "123-456-7890", "9 AM - 5 PM", true
    );

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(existingService));
    when(serviceRepository.save(any(ServiceEntity.class))).thenReturn(updatedService);

    // Act
    ServiceEntity result = serviceService.updateService(serviceId, updateData);

    // Assert
    assertNotNull(result, "The updated service should not be null");
    assertEquals("Partial Updated Service Name", result.getName(),
            "Service name should be updated");
    assertEquals("Partial New City", result.getCity(),
            "Service city should be updated");
    // Ensure other fields remain unchanged
    assertEquals("shelters", result.getCategory(),
            "Service category should remain unchanged");
    assertEquals(40.7128, result.getLatitude(),
            "Service latitude should remain unchanged");
    assertEquals(-74.0060, result.getLongitude(),
            "Service longitude should remain unchanged");
    assertEquals("123 Old St", result.getAddress(),
            "Service address should remain unchanged");
    assertEquals("10001", result.getZipcode(),
            "Service zipcode should remain unchanged");
    assertEquals("123-456-7890", result.getContactNumber(),
            "Service contact number should remain unchanged");
    assertEquals("9 AM - 5 PM", result.getOperationHour(),
            "Service operation hour should remain unchanged");
    assertTrue(result.getAvailability(),
            "Service availability should remain unchanged");
    verify(serviceRepository, times(1)).findById(serviceId);
    verify(serviceRepository, times(1)).save(existingService);
  }

  /**
   * Tests the updateService method when attempting to update a non-existing service.
   * Expects the method to return null.
   */
  @Test
  public void testUpdateService_NonExistingId_ShouldReturnNull() {
    // Arrange
    Long serviceId = 999L;
    ServiceEntity updateData = new ServiceEntity();
    updateData.setName("Non-Existent Service Name");

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

    // Act
    ServiceEntity result = serviceService.updateService(serviceId, updateData);

    // Assert
    assertNull(result, "Updating a non-existing service should return null");
    verify(serviceRepository, times(1)).findById(serviceId);
    verify(serviceRepository, times(0)).save(any(ServiceEntity.class));
  }

  /**
   * Tests the updateService method when the save operation violates data integrity.
   * Expects a DataIntegrityViolationException to be thrown.
   */
  @Test
  public void testUpdateService_SaveThrowsException_ShouldThrowException() {
    // Arrange
    Long serviceId = 1L;
    ServiceEntity existingService = new ServiceEntity(
            serviceId, "Old Service Name", "shelters",
            40.7128, -74.0060,
            "123 Old St", "Old City", "NY", "10001",
            "123-456-7890", "9 AM - 5 PM", true
    );

    ServiceEntity updateData = new ServiceEntity(
            serviceId, "Updated Service Name", "shelters",
            40.7128, -74.0060,
            "456 New St", "New City", "NY", "10002",
            "987-654-3210", "10 AM - 6 PM", false
    );

    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(existingService));
    when(serviceRepository.save(any(ServiceEntity.class)))
            .thenThrow(new DataIntegrityViolationException("Database constraint violated"));

    // Act & Assert
    Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
      serviceService.updateService(serviceId, updateData);
    });

    assertEquals("Database constraint violated", exception.getMessage(),
            "Exception message should match the expected message");
    verify(serviceRepository, times(1)).findById(serviceId);
    verify(serviceRepository, times(1)).save(existingService);
  }


  /**
   * Tests the deleteServiceById method when deleting an existing service.
   * Expects the method to return true.
   */
  @Test
  public void testDeleteServiceById_ExistingId_ShouldReturnTrue() {
    // Arrange
    Long serviceId = 1L;

    when(serviceRepository.existsById(serviceId)).thenReturn(true);
    doNothing().when(serviceRepository).deleteById(serviceId);

    // Act
    boolean result = serviceService.deleteServiceById(serviceId);

    // Assert
    assertTrue(result, "The service should be successfully deleted");
    verify(serviceRepository, times(1)).existsById(serviceId);
    verify(serviceRepository, times(1)).deleteById(serviceId);
  }

  /**
   * Tests the deleteServiceById method when attempting to delete a non-existing service.
   * Expects the method to return false.
   */
  @Test
  public void testDeleteServiceById_NonExistingId_ShouldReturnFalse() {
    // Arrange
    Long serviceId = 999L;

    when(serviceRepository.existsById(serviceId)).thenReturn(false);

    // Act
    boolean result = serviceService.deleteServiceById(serviceId);

    // Assert
    assertFalse(result, "Deleting a non-existing service should return false");
    verify(serviceRepository, times(1)).existsById(serviceId);
    verify(serviceRepository, times(0)).deleteById(anyLong());
  }
}
