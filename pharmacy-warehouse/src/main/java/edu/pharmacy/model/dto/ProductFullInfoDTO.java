package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProductFullInfoDTO {

    private long id;
    private String name;
    private String activeSubstanceName;
    private double price;
    private long supplierId;
    private String supplierName;
}
