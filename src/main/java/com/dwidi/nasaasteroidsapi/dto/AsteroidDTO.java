package com.dwidi.nasaasteroidsapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AsteroidDTO {
    private String id;
    private String name;
    private Double absoluteMagnitude;
    private Double estimatedDiameterMin;
    private Double estimatedDiameterMax;
    private Double missDistanceKilometers;
    private Boolean isPotentiallyHazardousAsteroid;

    public AsteroidDTO(String id, String name, Double absoluteMagnitude, Double estimatedDiameterMin, Double estimatedDiameterMax, Double missDistanceKilometers, Boolean isPotentiallyHazardousAsteroid) {
        this.id = id;
        this.name = name;
        this.absoluteMagnitude = absoluteMagnitude;
        this.estimatedDiameterMin = estimatedDiameterMin;
        this.estimatedDiameterMax = estimatedDiameterMax;
        this.missDistanceKilometers = missDistanceKilometers;
        this.isPotentiallyHazardousAsteroid = isPotentiallyHazardousAsteroid;
    }
}
