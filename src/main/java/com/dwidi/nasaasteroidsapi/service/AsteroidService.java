package com.dwidi.nasaasteroidsapi.service;

import com.dwidi.nasaasteroidsapi.dto.AsteroidDTO;
import com.dwidi.nasaasteroidsapi.dto.CountResponseDTO;
import com.dwidi.nasaasteroidsapi.dto.TopAsteroidsResponseDTO;
import com.dwidi.nasaasteroidsapi.exception.AsteroidNotFoundException;
import com.dwidi.nasaasteroidsapi.model.Asteroid;
import com.dwidi.nasaasteroidsapi.model.CloseApproachData;
import com.dwidi.nasaasteroidsapi.model.NasaApiResponse;
import com.dwidi.nasaasteroidsapi.model.NasaApiResponseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AsteroidService {

    @Value("${nasa.api.key}")
    private String apiKey;

    @Value("${nasa.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    public TopAsteroidsResponseDTO getTop10ClosestAsteroids(String startDate, String endDate) {
        String url = String.format("%s/neo/rest/v1/feed?start_date=%s&end_date=%s&api_key=%s", apiUrl, startDate, endDate, apiKey);
        ResponseEntity<NasaApiResponse> response = restTemplate.getForEntity(url, NasaApiResponse.class);

        Map<String, List<Asteroid>> nearEarthObjects = Objects.requireNonNull(response.getBody()).getNear_earth_objects();

        List<AsteroidDTO> allAsteroids = nearEarthObjects.values().stream()
                .flatMap(List::stream)
                .map(this::convertToDto)
                .sorted(Comparator.comparingDouble(AsteroidDTO::getMissDistanceKilometers))
                .limit(10)
                .collect(Collectors.toList());

        return new TopAsteroidsResponseDTO(allAsteroids);
    }


    public AsteroidDTO getAsteroidById(String id) {
        String url = String.format("%s/neo/rest/v1/neo/%s?api_key=%s", apiUrl, id, apiKey);
        try {
            ResponseEntity<Asteroid> response = restTemplate.getForEntity(url, Asteroid.class);

            if (response.getBody() == null) {
                throw new AsteroidNotFoundException("Asteroid with ID " + id + " not found");
            }

            Asteroid asteroid = response.getBody();
            return convertToDto(asteroid);
        } catch (HttpClientErrorException.NotFound e) {
            throw new AsteroidNotFoundException("Asteroid with ID " + id + " not found");
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("An error occurred while fetching the asteroid with ID " + id, e);
        }
    }

    public CountResponseDTO countAsteroidsByMinDistance(double minDistance) {
        String url = String.format("%s/neo/rest/v1/neo/browse?api_key=%s", apiUrl, apiKey);
        ResponseEntity<NasaApiResponseFilter> response = restTemplate.getForEntity(url, NasaApiResponseFilter.class);

        long count = Objects.requireNonNull(response.getBody()).getNear_earth_objects().stream()
                .filter(asteroid -> asteroid.getClose_approach_data() != null && !asteroid.getClose_approach_data().isEmpty())
                .filter(asteroid -> {
                    CloseApproachData closestApproach = asteroid.getClose_approach_data().get(0);
                    return Double.parseDouble(closestApproach.getMiss_distance().getKilometers()) >= minDistance;
                })
                .count();

        return new CountResponseDTO(count);
    }

    private AsteroidDTO convertToDto(Asteroid asteroid) {
        if (asteroid.getClose_approach_data() == null || asteroid.getClose_approach_data().isEmpty()) {
            throw new IllegalArgumentException("Asteroid does not have close approach data");
        }
        CloseApproachData closestApproach = asteroid.getClose_approach_data().get(0);
        return new AsteroidDTO(
                asteroid.getId(),
                asteroid.getName(),
                asteroid.getAbsolute_magnitude_h(),
                asteroid.getEstimated_diameter().getKilometers().getEstimated_diameter_min(),
                asteroid.getEstimated_diameter().getKilometers().getEstimated_diameter_max(),
                Double.parseDouble(String.valueOf(closestApproach.getMiss_distance().getKilometers())),
                asteroid.is_potentially_hazardous_asteroid()
        );
    }
}
