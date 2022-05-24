package edu.pharmacy.dao;

import edu.pharmacy.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
}
