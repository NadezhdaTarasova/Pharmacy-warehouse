package edu.pharmacy.dao;

import edu.pharmacy.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {

    boolean existsByName(String name);
    City getByName(String name);
}
