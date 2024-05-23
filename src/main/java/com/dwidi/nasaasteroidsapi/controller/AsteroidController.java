package com.dwidi.nasaasteroidsapi.controller;

import com.dwidi.nasaasteroidsapi.dto.*;
import com.dwidi.nasaasteroidsapi.service.AsteroidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/asteroids")
public class AsteroidController {

    private final AsteroidService asteroidService;

    public AsteroidController(AsteroidService asteroidService) {
        this.asteroidService = asteroidService;
    }

    @GetMapping("/closest")
    public ResponseEntity<TopAsteroidsResponseDTO> getTop10ClosestAsteroids(@RequestBody DateRangeRequestDTO dateRangeRequest) {
        TopAsteroidsResponseDTO response = asteroidService.getTop10ClosestAsteroids(dateRangeRequest.getStartDate(), dateRangeRequest.getEndDate());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsteroidResponseDTO> getAsteroidById(@PathVariable String id) {
        AsteroidDTO asteroid = asteroidService.getAsteroidById(id);
        AsteroidResponseDTO response = new AsteroidResponseDTO(Collections.singletonList(asteroid));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count-by-distance")
    public ResponseEntity<CountResponseDTO> countAsteroidsByMinDistance(@RequestBody DistanceRequestDTO distanceRequest) {
        CountResponseDTO countResponse = asteroidService.countAsteroidsByMinDistance(distanceRequest.getDistance());
        return ResponseEntity.ok(countResponse);
    }
}

