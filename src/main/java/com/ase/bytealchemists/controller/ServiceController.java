package com.ase.bytealchemists.controller;

import com.ase.bytealchemists.model.ServiceEntity;
import com.ase.bytealchemists.service.ServiceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for managing service queries in the Homeless Support API.
 * This controller provides an endpoint to query services such as shelters,
 * food banks, healthcare centers, and more, based on
 * latitude, longitude, category, and availability.
 *
 * @author Byte Alchemists
 * @version 1.0
 */
@RestController
@RequestMapping("/services")
public class ServiceController {

  @Autowired
  private ServiceService serviceService;

  /**
   * Queries services based on the provided latitude, longitude, category, and availability filters.
   *
   * @param latitude     the latitude for filtering services based on location (optional)
   * @param longitude    the longitude for filtering services based on location (optional)
   * @param category     the category of services to filter
   *                     (e.g., shelter, food bank) (optional)
   * @param availability the availability status to filter services
   *                     (true for available, false for unavailable) (optional)
   * @return a list of services that match the given filters
   */
  @GetMapping("/query")
  public ResponseEntity<List<ServiceEntity>> queryServices(
      @RequestParam(value = "latitude", required = false) Double latitude,
      @RequestParam(value = "longitude", required = false) Double longitude,
      @RequestParam(value = "category", required = false) String category,
      @RequestParam(value = "availability", required = false) Boolean availability) {

    // Call the service layer to get the filtered results
    List<ServiceEntity> services =
        serviceService.queryServices(latitude, longitude, category, availability);

    // Return the result with HTTP 200 OK status
    return ResponseEntity.ok(services);
  }

  /**
   * Deletes a service by its ID.
   *
   * @param id the ID of the service to delete
   * @return a ResponseEntity with appropriate HTTP status
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteService(@PathVariable Long id) {
    boolean isDeleted = serviceService.deleteServiceById(id);
    if (isDeleted) {
      // Deletion successful, return HTTP 204 No Content
      return ResponseEntity.noContent().build();
    } else {
      // Service not found, return HTTP 404 Not Found
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Updates a service by its ID.
   *
   * @param id      the ID of the service to update
   * @param service the updated service data
   * @return a ResponseEntity containing the updated service and appropriate HTTP status
   */
  @PutMapping("/{id}")
  public ResponseEntity<ServiceEntity> updateService(@PathVariable Long id, @RequestBody ServiceEntity service) {
    ServiceEntity updatedService = serviceService.updateService(id, service);
    if (updatedService != null) {
      // Update successful, return the updated service with HTTP 200 OK
      return ResponseEntity.ok(updatedService);
    } else {
      // Service not found, return HTTP 404 Not Found
      return ResponseEntity.notFound().build();
    }
  }
}
