package com.dwidi.nasaasteroidsapi.controller;

import com.dwidi.nasaasteroidsapi.dto.*;
import com.dwidi.nasaasteroidsapi.exception.BadRequestException;
import com.dwidi.nasaasteroidsapi.service.AsteroidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/asteroids")
public class AsteroidController {

    @Autowired
    private AsteroidService asteroidService;


    @GetMapping("/closest")
    public ResponseEntity<TopAsteroidsResponseDTO> getTop10ClosestAsteroids(@RequestParam String startDate, String endDate) {
        try {
            TopAsteroidsResponseDTO response = asteroidService.getTop10ClosestAsteroids(startDate, endDate);
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            // Return an empty list in case of an error with status and detail
            TopAsteroidsResponseDTO errorResponse = new TopAsteroidsResponseDTO();
            errorResponse.setAsteroids(Collections.emptyList());
            errorResponse.setStatus(400);
            errorResponse.setDetail(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
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

