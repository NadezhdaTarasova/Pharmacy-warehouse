package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AddressToUpdateDTO {

    private long streetId;
    private String house;
}
