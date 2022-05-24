package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AccountFullInfoDTO {

    private String iban;
    private double balance;
    private long organizationId;
    private String organizationType;
    private String organizationName;
}
