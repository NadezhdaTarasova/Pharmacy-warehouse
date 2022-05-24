package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class OrderToCreateDTO {

    private long pharmacyId;
    private long employeeId;
    private List<OrderItemDTO> productsAndAmounts;
}
