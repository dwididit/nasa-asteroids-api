package com.dwidi.nasaasteroidsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsteroidModel {
    private String id;
    private String name;
    private Double absolute_magnitude_h;
    private EstimatedDiameter estimated_diameter;
    private boolean is_potentially_hazardous_asteroid;
    private List<CloseApproachData> close_approach_data;
}

