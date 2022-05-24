package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class EmployeeFullInfoDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
    private String position;
    private long pharmacyId;
    private String pharmacyName;
    private boolean isResponsibleForWarehouse;
    private long warehouseId;
}
