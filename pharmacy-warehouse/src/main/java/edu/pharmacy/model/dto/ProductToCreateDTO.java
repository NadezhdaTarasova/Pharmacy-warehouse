package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProductToCreateDTO {

    private long supplierId;
    private String name;
    private String activeSubstanceName;
    private double price;
}
