package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WarehouseFullInfoDTO {

    private long id;
    private String phone;
    private long pharmacyId;
    private String pharmacyName;
    private String cityName;
    private String streetName;
    private String house;
    private long responsibleEmployeeId;
    private String responsibleEmployeeName;
}
