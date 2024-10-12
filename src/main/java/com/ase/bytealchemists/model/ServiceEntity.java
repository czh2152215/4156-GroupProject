package com.ase.bytealchemists.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a service in the Homeless Support API.
 * This class maps to the "service" table in the database and represents a service such as
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

  private String name;
  private String category;
  private double latitude;
  private double longitude;
  private String address;
  private String city;
  private String state;
  private String zipcode;
  private String contactNumber;
  private String operationHour;
  private boolean availability;

}
