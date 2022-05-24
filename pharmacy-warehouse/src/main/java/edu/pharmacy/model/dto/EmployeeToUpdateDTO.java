package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class EmployeeToUpdateDTO {

    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
}
