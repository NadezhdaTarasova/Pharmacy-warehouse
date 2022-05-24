package edu.pharmacy.dao;

import edu.pharmacy.model.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    boolean existsByName(String name);

    boolean existsByPhone(String phone);
}
