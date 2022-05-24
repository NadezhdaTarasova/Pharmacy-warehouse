package edu.pharmacy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PharmacyFullInfoDTO {

    private long id;
    private String name;
    private String phone;
    private String cityName;
    private String streetName;
    private String house;
    private String accountIban;
    private double accountBalance;
    private long warehouseId;
}
