package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SupplierFullInfoDTO {

    private long id;
    private String name;
    private String phone;
    private String cityName;
    private String streetName;
    private String house;
    private String accountIban;
    private double accountBalance;
}
