package com.ase.bytealchemists.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.ase.bytealchemists.model.ServiceEntity;
import com.ase.bytealchemists.repository.ServiceRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
}
