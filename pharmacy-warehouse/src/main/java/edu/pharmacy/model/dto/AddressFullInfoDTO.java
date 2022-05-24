package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AddressFullInfoDTO {

    private long id;
    private String cityName;
    private String streetName;
    private String house;
    private long organizationId;
    private String organizationType;
    private String organizationName;
}
