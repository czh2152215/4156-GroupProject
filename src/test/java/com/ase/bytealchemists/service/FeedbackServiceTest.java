package com.ase.bytealchemists.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ase.bytealchemists.model.FeedbackEntity;
import com.ase.bytealchemists.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Class FeedbackServiceTest.
 *
 * @author Jason
 * @version 2024/11/23
 */
public class FeedbackServiceTest {
  @Mock
  private FeedbackRepository feedbackRepository;

  @InjectMocks
  private FeedbackService feedbackService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this); // Initialize mocks
  }

  /**
   * Tests creating feedback successfully.
   */
  @Test
  public void testCreateFeedback_Success() {
    // Arrange
    FeedbackEntity feedback = new FeedbackEntity();
    feedback.setUserId(1L);
    feedback.setServiceId(101L);
    feedback.setRating(5);
    feedback.setComment("Great service!");

    when(feedbackRepository.save(any(FeedbackEntity.class))).thenReturn(feedback);

    // Act
    FeedbackEntity result = feedbackService.createFeedback(feedback);

    // Assert
    assertNotNull(result, "The saved feedback should not be null");
    assertEquals(1L, result.getUserId(), "User ID should match");
    assertEquals(101L, result.getServiceId(), "Service ID should match");
    assertEquals(5, result.getRating(), "Rating should match");
    assertEquals("Great service!", result.getComment(), "Comment should match");

    verify(feedbackRepository, times(1)).save(feedback);
  }
}

