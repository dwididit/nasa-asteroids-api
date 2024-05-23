package com.dwidi.nasaasteroidsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diameter {
    private double estimated_diameter_min;
    private double estimated_diameter_max;
}
