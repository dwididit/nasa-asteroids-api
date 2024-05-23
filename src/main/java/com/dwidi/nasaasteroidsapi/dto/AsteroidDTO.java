package com.dwidi.nasaasteroidsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsteroidDTO {
    private String id;
    private String name;
    private double absoluteMagnitude;
    private double estimatedDiameterMin;
    private double estimatedDiameterMax;
    private double missDistanceKilometers;
    private boolean isPotentiallyHazardousAsteroid;
}
