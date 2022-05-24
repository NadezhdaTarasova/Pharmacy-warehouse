package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class EmployeeToCreateDTO {

    private long pharmacyId;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String position;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
}
