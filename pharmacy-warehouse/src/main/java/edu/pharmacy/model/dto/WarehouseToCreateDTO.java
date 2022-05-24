package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WarehouseToCreateDTO {

    private long pharmacyId;
    private long employeeId;
    private String phone;
}
