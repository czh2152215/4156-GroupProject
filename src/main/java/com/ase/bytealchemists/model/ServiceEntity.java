package com.ase.bytealchemists.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a service in the Homeless Support API.
 * This class maps to the "service" table in the database and represents a
 * service such as
 * shelters, food banks, healthcare centers,
 * and other services that homeless individuals can access.
 *
 * @author Jason
 * @version 1.0
 */
@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Service name cannot be blank")
  @Column(nullable = false)
  private String name;

  @NotBlank(message = "Category cannot be blank")
  private String category;

  // Latitude: between -90 and 90
  @DecimalMin(value = "-90.0", inclusive = true, message = "Latitude must be >= -90")
  @DecimalMax(value = "90.0", inclusive = true, message = "Latitude must be <= 90")
  private double latitude;

  // Longitude: between -180 and 180
  @DecimalMin(value = "-180.0", inclusive = true, message = "Longitude must be >= -180")
  @DecimalMax(value = "180.0", inclusive = true, message = "Longitude must be <= 180")
  private double longitude;

  @NotBlank(message = "Address cannot be blank")
  private String address;

  @NotBlank(message = "City cannot be blank")
  private String city;

  @NotBlank(message = "State cannot be blank")
  private String state;

  @Pattern(regexp = "\\d{5}", message = "Zipcode must be a 5-digit number")
  private String zipcode;

  private String contactNumber;

  @Pattern(regexp = "\\d{1,2} (AM|PM) - \\d{1,2} (AM|PM)", 
        message = "Operation hours must follow the format: '9 AM - 5 PM'")
  private String operationHour;

  private boolean availability;

}
