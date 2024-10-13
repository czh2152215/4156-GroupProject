package com.ase.bytealchemists.controller;

import com.ase.bytealchemists.model.ServiceEntity;
import com.ase.bytealchemists.service.CategoryService;
import com.ase.bytealchemists.service.ServiceService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

  @Autowired
  private CategoryService categoryService;

  /**
   * Queries services based on the provided latitude, longitude, category, and
   * availability filters.
   *
   * @param latitude     the latitude for filtering services based on location
   *                     (optional)
   * @param longitude    the longitude for filtering services based on location
   *                     (optional)
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
    List<ServiceEntity> services = serviceService.queryServices(
        latitude, longitude, category, availability);

    // Return the result with HTTP 200 OK status
    return ResponseEntity.ok(services);
  }

  /**
   * End point for registering a new service.
   * This method validates the service entity and checks if the specified
   * category exists. If there are validation errors or the category
   * does not exist, it returns a 400 (Bad Request) response.
   * If all checks pass, the service is registered and a 201 (Created)
   * response is returned.
   *
   * @param serviceEntity the service entity to be registered,
   *                      containing service details like name, category,
   *                      and location information.
   * 
   * @param bindingResult the binding result that holds validation errors
   *                      if the service entity is invalid.
   * 
   * @return a ResponseEntity containing the result of the registration.
   *         Returns HTTP 400 if there are validation errors or the category
   *         does not exist. Returns HTTP 201 if the service is registered
   *         successfully.
   */
  @PostMapping
  public ResponseEntity<?> registerService(
      @Valid @RequestBody ServiceEntity serviceEntity,
      BindingResult bindingResult) {
    // Check if there are validation errors
    if (bindingResult.hasErrors()) {
      Map<String, String> errors = new HashMap<>();
      for (FieldError error : bindingResult.getFieldErrors()) {
        errors.put(error.getField(), error.getDefaultMessage());
      }
      // Return the errors with HTTP 400 (Bad Request)
      return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Check if the category exists
    if (!categoryService.categoryExists(serviceEntity.getCategory())) {
      return new ResponseEntity<>("Category does not exist", HttpStatus.BAD_REQUEST);
    }

    // Save the service if validation and category checks pass
    serviceService.registerService(serviceEntity);
    return new ResponseEntity<>("Service registered successfully", HttpStatus.CREATED);
  }
}