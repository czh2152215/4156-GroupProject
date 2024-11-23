package com.ase.bytealchemists.controller;

import com.ase.bytealchemists.model.FeedbackEntity;
import com.ase.bytealchemists.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing feedback-related API endpoints.
 *
 * @author Jason
 * @version 1.0
 */
@RestController
@RequestMapping("/services")
public class FeedbackController {
  @Autowired
  private FeedbackService feedbackService;

  /**
   * Endpoint to create a new feedback entry.
   *
   * @param feedbackEntity The feedback details from the request body.
   * @return The created feedback entity with HTTP status 201 (Created) on success,
   *         or HTTP status 400 (Bad Request) if validation fails.
   */
  @PostMapping("/feedback")
  public ResponseEntity<?> createFeedback(@Valid @RequestBody FeedbackEntity feedbackEntity) {
    try {
      // Save the feedback using the service layer
      FeedbackEntity savedFeedback = feedbackService.createFeedback(feedbackEntity);

      // Return the created feedback with HTTP status 201
      return new ResponseEntity<>(savedFeedback, HttpStatus.CREATED);
    } catch (Exception e) {
      // Return error response if any issue occurs
      return new ResponseEntity<>("An error occurred while creating the feedback.",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * End point for deleting a feedback by id.
   * This method attempts to delete a feedback by the specified id.
   *
   * @param id the ID of the feedback to be deleted.
   * @return A {@code ResponseEntity} object containing an HTTP 200 response
   *         with an appropriate message or the proper status code in tune
   *         with what has happened.
   */
  @DeleteMapping(value = "/feedback/{feedback_id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deleteFeedbackById(@PathVariable("feedback_id") int id) {
    try {
      if (feedbackService.deleteFeedbackById(id)) {
        return new ResponseEntity<>("Feedback was deleted successfully.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("This feedback does not exist.", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  public ResponseEntity<?> handleException(Exception e) {
    System.out.println(e.toString());
    return new ResponseEntity<>("An Error has occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
