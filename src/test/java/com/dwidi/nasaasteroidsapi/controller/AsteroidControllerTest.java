package com.dwidi.nasaasteroidsapi.controller;

import com.dwidi.nasaasteroidsapi.dto.*;
import com.dwidi.nasaasteroidsapi.service.AsteroidService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AsteroidControllerTest {

    @InjectMocks
    private AsteroidController asteroidController;

    @Mock
    private AsteroidService asteroidService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTop10ClosestAsteroids() {
        // Arrange
        String startDate = "2023-01-01";
        String endDate = "2023-01-08";

        TopAsteroidsResponseDTO responseDTO = new TopAsteroidsResponseDTO();
        when(asteroidService.getTop10ClosestAsteroids(anyString(), anyString())).thenReturn(responseDTO);

        // Act
        ResponseEntity<TopAsteroidsResponseDTO> response = asteroidController.getTop10ClosestAsteroids(startDate, endDate);

        // Assert
        assertEquals(ResponseEntity.ok(responseDTO), response);
    }

    @Test
    void testGetAsteroidById() {
        // Arrange
        String asteroidId = "12345";
        AsteroidDTO asteroidDTO = new AsteroidDTO();
        AsteroidResponseDTO responseDTO = new AsteroidResponseDTO(Collections.singletonList(asteroidDTO));
        when(asteroidService.getAsteroidById(anyString())).thenReturn(asteroidDTO);

        // Act
        ResponseEntity<AsteroidResponseDTO> response = asteroidController.getAsteroidById(asteroidId);

        // Assert
        assertEquals(ResponseEntity.ok(responseDTO), response);
    }

    @Test
    void testCountAsteroidsByMinDistance() {
        // Arrange
        double minDistance = 1000000;

        CountResponseDTO responseDTO = new CountResponseDTO();
        when(asteroidService.countAsteroidsByMinDistance(anyDouble())).thenReturn(responseDTO);

        // Act
        ResponseEntity<CountResponseDTO> response = asteroidController.countAsteroidsByMinDistance(String.valueOf(minDistance));

        // Assert
        assertEquals(ResponseEntity.ok(responseDTO), response);
    }

    @Test
    void testStoreTop10Asteroids() {
        // Arrange
        String startDate = "2023-01-01";
        String endDate = "2023-01-08";
        ExceptionResponseDTO<String> responseDTO = new ExceptionResponseDTO<>();
        responseDTO.setStatus(200);
        responseDTO.setMessage("Successfully saved top 10 closest asteroids to the database");
        responseDTO.setDetails("Your database is updated");

        when(asteroidService.saveTop10ClosestAsteroids(anyString(), anyString())).thenReturn(responseDTO);

        // Act
        ResponseEntity<ExceptionResponseDTO<String>> response = asteroidController.storeTop10Asteroids(startDate, endDate);

        // Assert
        assertEquals(ResponseEntity.ok(responseDTO), response);
    }
}
