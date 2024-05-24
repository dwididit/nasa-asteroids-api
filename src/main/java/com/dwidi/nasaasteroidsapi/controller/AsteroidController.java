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
    public ResponseEntity<TopAsteroidsResponseDTO> getTop10ClosestAsteroids(@RequestParam String startDate, String endDate) {
        TopAsteroidsResponseDTO response = asteroidService.getTop10ClosestAsteroids(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsteroidResponseDTO> getAsteroidById(@PathVariable String id) {
        AsteroidDTO asteroid = asteroidService.getAsteroidById(id);
        AsteroidResponseDTO response = new AsteroidResponseDTO(Collections.singletonList(asteroid));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count-by-distance")
    public ResponseEntity<CountResponseDTO> countAsteroidsByMinDistance(@RequestParam String distance) {
        CountResponseDTO countResponse = asteroidService.countAsteroidsByMinDistance(Double.parseDouble(distance));
        return ResponseEntity.ok(countResponse);
    }

    @PostMapping("/store-top10")
    public ResponseEntity<ExceptionResponseDTO<String>> storeTop10Asteroids(@RequestParam String startDate, @RequestParam String endDate) {
        ExceptionResponseDTO<String> response = asteroidService.saveTop10ClosestAsteroids(startDate, endDate);
        return ResponseEntity.ok(response);
    }
}

