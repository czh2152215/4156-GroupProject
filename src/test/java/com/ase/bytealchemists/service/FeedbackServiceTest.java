package com.ase.bytealchemists.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ase.bytealchemists.model.CategoryEntity;
import com.ase.bytealchemists.model.FeedbackEntity;
import com.ase.bytealchemists.repository.FeedbackRepository;
import java.util.Optional;
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

  /**
   * Test deleteFeedbackById() method when feedback is deleted successfully.
   */
  @Test
  public void testDeleteFeedbackByIdWhenSuccess() {
    FeedbackEntity feedbackEntity = new FeedbackEntity(1L, 1L, 101L, 5,
                                              "Great service!");

    when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedbackEntity));
    doNothing().when(feedbackRepository).deleteById(1L);

    boolean result = feedbackService.deleteFeedbackById(1);

    // Assert
    verify(feedbackRepository, times(1)).findById(1L);
    verify(feedbackRepository, times(1)).deleteById(1L);
    assertTrue(result, "The feedback should be successfully deleted");
  }

  /**
   * Test deleteFeedbackById() method when feedback does not existt.
   */
  @Test
  public void testDeleteFeedbackByIdWhenFail() {
    when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

    boolean result = feedbackService.deleteFeedbackById(1);

    // Assert
    verify(feedbackRepository, times(1)).findById(1L);
    verify(feedbackRepository, times(0)).deleteById(1L);
    assertFalse(result, "The method should return false when feedback does not exist");
  }

  /**
   * Tests retrieving a feedback by its ID when it exists.
   */
  @Test
  public void testGetFeedbackById_Success() {
    // Arrange
    FeedbackEntity feedback = new FeedbackEntity(1L, 1L, 101L, 5, "Excellent service!");

    when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));

    // Act
    Optional<FeedbackEntity> result = feedbackService.getFeedbackById(1L);

    // Assert
    assertTrue(result.isPresent(), "Feedback should be present");
    assertEquals(1L, result.get().getId(), "Feedback ID should match");
    assertEquals(1L, result.get().getUserId(), "User ID should match");
    assertEquals(101L, result.get().getServiceId(), "Service ID should match");
    assertEquals(5, result.get().getRating(), "Rating should match");
    assertEquals("Excellent service!", result.get().getComment(), "Comment should match");

    verify(feedbackRepository, times(1)).findById(1L);
  }

  /**
   * Tests retrieving a feedback by its ID when it does not exist.
   */
  @Test
  public void testGetFeedbackById_NotFound() {
    // Arrange
    when(feedbackRepository.findById(999L)).thenReturn(Optional.empty());

    // Act
    Optional<FeedbackEntity> result = feedbackService.getFeedbackById(999L);

    // Assert
    assertFalse(result.isPresent(), "Feedback should not be present");
    verify(feedbackRepository, times(1)).findById(999L);
  }
}

