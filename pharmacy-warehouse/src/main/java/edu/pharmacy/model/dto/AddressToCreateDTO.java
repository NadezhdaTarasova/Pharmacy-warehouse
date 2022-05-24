package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AddressToCreateDTO {

    private long streetId;
    private String house;
    private long pharmacyId;
    private long warehouseId;
    private long supplierId;
}
