package com.dwidi.nasaasteroidsapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AsteroidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String asteroidId;
    private String name;
    private Double absoluteMagnitude;
    private Double estimatedDiameterMin;
    private Double estimatedDiameterMax;
    private Double missDistanceKilometers;
    private Boolean potentiallyHazardousAsteroid;
}
