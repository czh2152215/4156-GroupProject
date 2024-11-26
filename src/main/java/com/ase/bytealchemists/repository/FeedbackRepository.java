package com.ase.bytealchemists.repository;

import com.ase.bytealchemists.model.FeedbackEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing feedback entities in the database.
 * This interface extends {@link JpaRepository}
 * to provide CRUD operations for {@link com.ase.bytealchemists.model.FeedbackEntity}.
 *
 * @author Jason
 * @version 1.0
 */
@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
  /**
   * Finds all feedback entities by the associated service ID.
   *
   * @param serviceId The ID of the service to retrieve feedback for.
   * @return A list of feedback entities.
   */
  List<FeedbackEntity> findAllByServiceId(Long serviceId);
}
