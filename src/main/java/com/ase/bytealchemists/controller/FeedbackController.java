package com.ase.bytealchemists.controller;

import com.ase.bytealchemists.model.FeedbackEntity;
import com.ase.bytealchemists.service.FeedbackService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  /**
   * Endpoint to retrieve a single feedback by its ID.
   *
   * @param feedbackId The ID of the feedback to retrieve.
   * @return If found, returns the feedback entity and HTTP status 200 (OK);
   *         If not found, returns HTTP status 404 (Not Found) and an error message;
   *         In case of other exceptions, returns HTTP status 500 (Internal Server Error).
   */
  @GetMapping("/feedback/{feedbackId}")
  public ResponseEntity<?> getFeedbackById(@PathVariable("feedbackId") Long feedbackId) {
    try {
      // Retrieve feedback using the service layer
      Optional<FeedbackEntity> feedbackOptional = feedbackService.getFeedbackById(feedbackId);

      if (feedbackOptional.isPresent()) {
        // If feedback exists, return the feedback and HTTP status 200
        return new ResponseEntity<>(feedbackOptional.get(), HttpStatus.OK);
      } else {
        // If feedback does not exist, return HTTP status 404 and an error message
        return new ResponseEntity<>("Feedback with ID "
                + feedbackId + " not found.", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      // Handle other exceptions
      return handleException(e);
    }
  }


  public ResponseEntity<?> handleException(Exception e) {
    System.out.println(e.toString());
    return new ResponseEntity<>("An Error has occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
