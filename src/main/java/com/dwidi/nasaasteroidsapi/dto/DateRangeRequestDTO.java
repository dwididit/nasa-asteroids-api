package com.dwidi.nasaasteroidsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateRangeRequestDTO {
    private String startDate;
    private String endDate;
}
