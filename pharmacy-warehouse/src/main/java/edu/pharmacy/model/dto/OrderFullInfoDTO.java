package edu.pharmacy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data
public class OrderFullInfoDTO {

    private long id;
    private LocalDate date;
    private LocalTime time;
    private long pharmacyId;
    private String pharmacyName;
}
