package com.ase.bytealchemists.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ase.bytealchemists.config.TestSecurityConfig;
import com.ase.bytealchemists.controller.FeedbackController;
import com.ase.bytealchemists.model.FeedbackEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Unit tests for FeedbackController.
 */
@Import(TestSecurityConfig.class)
@WebMvcTest(FeedbackController.class)
public class FeedbackControllerTest {
  // Class body here
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FeedbackService feedbackService;

  @Autowired
  private ObjectMapper objectMapper; // To convert objects to JSON

  /**
   * Tests the creation of feedback when all input is valid.
   */
  @Test
  public void testCreateFeedback_Success() throws Exception {
    // Arrange
    FeedbackEntity mockFeedback = new FeedbackEntity();
    mockFeedback.setUserId(1L);
    mockFeedback.setServiceId(101L);
    mockFeedback.setRating(5);
    mockFeedback.setComment("Great service!");

    when(feedbackService.createFeedback(any(FeedbackEntity.class))).thenReturn(mockFeedback);

    // Act & Assert
    mockMvc.perform(post("/services/feedback")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockFeedback))) // Convert object to JSON
        .andExpect(status().isCreated())
        .andExpect(content().json(objectMapper.writeValueAsString(mockFeedback)));

    verify(feedbackService, times(1)).createFeedback(any(FeedbackEntity.class));
  }

  /**
   * Tests the creation of feedback when the service throws an exception.
   */
  @Test
  public void testCreateFeedback_Exception() throws Exception {
    // Arrange
    FeedbackEntity mockFeedback = new FeedbackEntity();
    mockFeedback.setUserId(1L);
    mockFeedback.setServiceId(101L);
    mockFeedback.setRating(4);
    mockFeedback.setComment("Good service!");

    when(feedbackService.createFeedback(any(FeedbackEntity.class)))
        .thenThrow(new RuntimeException("Database error"));

    // Act & Assert
    mockMvc.perform(post("/services/feedback")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockFeedback))) // Convert object to JSON
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("An error occurred while creating the feedback."));

    verify(feedbackService, times(1)).createFeedback(any(FeedbackEntity.class));
  }
}

