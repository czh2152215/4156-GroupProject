package com.ase.byteAlchemists.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {

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

  // Constructors, Getters, and Setters (可以使用 Lombok 简化 @Getter @Setter)
}
