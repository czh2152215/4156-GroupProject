package com.ase.bytealchemists.service;

import com.ase.bytealchemists.model.FeedbackEntity;
import com.ase.bytealchemists.repository.FeedbackRepository;
import java.util.List;
import java.util.Optional;
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
    return feedbackRepository.save(feedbackEntity);
  }

  /**
   * Delete a feedback in the database by its id.
   * This method checks if a feedback with the specified id is present
   * in the database first. If found, then it can be deleted and return true;
   * else return false.
   *
   * @param id the name of the feedback to delete.
   * @return true if the feedback is deleted successfully,
   *         false if the feedback does not exist.
   */
  public boolean deleteFeedbackById(int id) {
    // Check if the specified feedback exists
    Optional<FeedbackEntity> feedbackEntity = feedbackRepository.findById((long) id);

    if (feedbackEntity.isPresent()) {
      // If the feedback exists, delete it
      feedbackRepository.deleteById((long) id);
      return true;
    } else {
      // If the feedback does not exist, return false
      return false;
    }
  }

  /**
   * Retrieves all feedback entries for a specific service ID.
   *
   * @param serviceId The ID of the service to retrieve feedback for.
   * @return A list of feedback entities associated with the service ID.
   */
  public List<FeedbackEntity> getFeedbackByServiceId(Long serviceId) {
    return feedbackRepository.findAllByServiceId(serviceId);
  }


  /**
   * Retrieves a feedback entry by its ID.
   *
   * @param id The ID of the feedback to retrieve.
   * @return The {@link FeedbackEntity} if found.
   */
  public Optional<FeedbackEntity> getFeedbackById(Long id) {
    return feedbackRepository.findById(id);
  }
}
