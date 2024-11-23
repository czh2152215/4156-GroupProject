package com.ase.bytealchemists.service;

import com.ase.bytealchemists.model.FeedbackEntity;
import com.ase.bytealchemists.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for managing feedback-related operations.
 *
 * @author Jason
 * @version 1.0
 */
@Service
public class FeedbackService {
  // Class body here
  @Autowired
  private FeedbackRepository feedbackRepository;

  /**
   * Creates a new feedback entry in the database.
   *
   * @param feedbackEntity The feedback details to save.
   * @return The saved feedback entity.
   */
  public FeedbackEntity createFeedback(FeedbackEntity feedbackEntity) {
    // Save the feedback to the database using the repository
    return feedbackRepository.save(feedbackEntity);
  }
}

