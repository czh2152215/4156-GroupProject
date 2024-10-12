package com.ase.bytealchemists.repository;

import com.ase.bytealchemists.model.ServiceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Service entities in the database.
 * This interface extends {@link JpaRepository}
 * to provide CRUD operations for {@link ServiceEntity}.
 * It also includes a custom query to filter services based on latitude, longitude, radius,
 * category, and availability. The query calculates the distance between the given coordinates
 * and the service's coordinates using the Haversine formula, allowing services within a
 * specific radius to be returned.
 *
 * @author Jason
 * @version 1.0
 */
@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

  /**
   * Finds services based on the given filters of
   * latitude, longitude, radius, category, and availability.
   *
   * @param latitude the latitude of the point from which to calculate distances
   * @param longitude the longitude of the point from which to calculate distances
   * @param radius the maximum distance (in kilometers) to search for services
   * @param category the category of services to filter (optional)
   * @param availability the availability of services to filter (optional)
   * @return a list of services that match the given filters
   */
  @Query("SELECT s FROM ServiceEntity s "
      + "WHERE (:category IS NULL OR s.category = :category) "
      + "AND (:availability IS NULL OR s.availability = :availability) "
      + "AND (:latitude IS NULL OR :longitude IS NULL OR "
      + "(6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) "
      + "* cos(radians(s.longitude) - radians(:longitude)) + sin(radians(:latitude)) "
      + "* sin(radians(s.latitude)))) < :radius)")
  List<ServiceEntity> findByFilters(
      @Param("latitude") Double latitude,
      @Param("longitude") Double longitude,
      @Param("radius") Double radius,
      @Param("category") String category,
      @Param("availability") Boolean availability);
}
