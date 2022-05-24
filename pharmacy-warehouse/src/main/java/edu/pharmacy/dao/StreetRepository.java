package edu.pharmacy.dao;

import edu.pharmacy.model.entity.City;
import edu.pharmacy.model.entity.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StreetRepository extends JpaRepository<Street, Long> {

    Street getByName(String name);

    boolean existsByName(String name);

    @Query(value = "SELECT * FROM streets s WHERE s.name =:name AND s.city_id =:cityId", nativeQuery = true)
    Street getByNameAndCityId(@Param("name") String name, @Param("cityId") long cityId);
}
