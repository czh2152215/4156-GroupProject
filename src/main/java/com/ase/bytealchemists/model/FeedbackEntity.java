package com.ase.bytealchemists.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class FeedbackEntity.
 *
 * @author Jason
 * @version 2024/11/23
 */
@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackEntity {
  // Class body here
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "User ID cannot be null")
  @Column(name = "user_id", nullable = false)
  private Long userId;

  @NotNull(message = "Service ID cannot be null")
  @Column(name = "service_id", nullable = false)
  private Long serviceId;

  @NotNull(message = "Rating cannot be null")
  @Min(value = 1, message = "Rating must be at least 1")
  @Max(value = 5, message = "Rating must be at most 5")
  @Column(nullable = false)
  private int rating;

  @Column(nullable = false)
  private String comment;
}

