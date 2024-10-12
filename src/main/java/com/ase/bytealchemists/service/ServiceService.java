package com.ase.bytealchemists.service;

import com.ase.bytealchemists.model.ServiceEntity;
import com.ase.bytealchemists.repository.ServiceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing operations related to services in the Homeless Support API.
 *
 * @author Jason
 * @version 1.0
 */
@Service
public class ServiceService {
  @Autowired
  private ServiceRepository serviceRepository;

  /**
   * Queries services based on latitude, longitude, category, and availability
   * within a fixed radius.
   *
   * <p>This method uses a fixed radius of 10 km to find services within a certain location
   * and applies additional filters such as service category and availability. It calls the
   * repository layer to retrieve the results.</p>
   *
   * @param latitude the latitude to filter services by location
   * @param longitude the longitude to filter services by location
   * @param category the category of services to filter (optional)
   * @param availability the availability status to filter services (optional)
   * @return a list of {@link ServiceEntity} that match the given filters
   */
  public List<ServiceEntity> queryServices(Double latitude, Double longitude,
                                           String category, Boolean availability) {
    double fixedRadius = 10.0;
    return serviceRepository.findByFilters(latitude, longitude,
        fixedRadius, category, availability);
  }
}
