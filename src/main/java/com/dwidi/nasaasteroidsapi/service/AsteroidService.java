package com.dwidi.nasaasteroidsapi.service;

import com.dwidi.nasaasteroidsapi.dto.*;
import com.dwidi.nasaasteroidsapi.entity.AsteroidEntity;
import com.dwidi.nasaasteroidsapi.exception.AsteroidNotFoundException;
import com.dwidi.nasaasteroidsapi.exception.BadRequestException;
import com.dwidi.nasaasteroidsapi.model.AsteroidModel;
import com.dwidi.nasaasteroidsapi.model.CloseApproachData;
import com.dwidi.nasaasteroidsapi.model.NasaApiResponse;
import com.dwidi.nasaasteroidsapi.model.NasaApiResponseFilter;
import com.dwidi.nasaasteroidsapi.repository.AsteroidRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AsteroidService {

    @Value("${nasa.api.key}")
    private String apiKey;

    @Value("${nasa.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AsteroidRepository asteroidRepository;

    private static final Pattern DATE_PATTERN = Pattern.compile(("\\d{4}-\\d{2}-\\d{2}"));

    public DateFormatResponseDTO validateDateFormat(String date) {
        if (!DATE_PATTERN.matcher(date).matches()) {
            return new DateFormatResponseDTO(400, "Date format should be YYYY-MM-DD");
        }
        return null;
    }

    public TopAsteroidsResponseDTO getTop10ClosestAsteroids(String startDate, String endDate) {
        // Validate date formats
        DateFormatResponseDTO startDateValidation = validateDateFormat(startDate);
        DateFormatResponseDTO endDateValidation = validateDateFormat(endDate);

        if (startDateValidation != null) {
            throw new BadRequestException("Invalid start date: " + startDateValidation.getDetail());
        }

        if (endDateValidation != null) {
            throw new BadRequestException("Invalid end date: " + endDateValidation.getDetail());
        }

        String url = String.format("%s/neo/rest/v1/feed?start_date=%s&end_date=%s&api_key=%s", apiUrl, startDate, endDate, apiKey);
        log.info("Get top 10 closest asteroids from URL: {}, from {} to {}", url, startDate, endDate);

        try {
            ResponseEntity<NasaApiResponse> response = restTemplate.getForEntity(url, NasaApiResponse.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new BadRequestException("Bad request");
            }

            Map<String, List<AsteroidModel>> nearEarthObjects = Objects.requireNonNull(response.getBody()).getNear_earth_objects();

            List<AsteroidDTO> allAsteroids = nearEarthObjects.values().stream()
                    .flatMap(List::stream)
                    .map(this::convertToDto)
                    .sorted(Comparator.comparingDouble(AsteroidDTO::getMissDistanceKilometers))
                    .limit(10)
                    .collect(Collectors.toList());

            return new TopAsteroidsResponseDTO(allAsteroids, 200, "Success");
        } catch (RestClientException e) {
            log.error("RestClientException: {}", e.getMessage());
            throw new BadRequestException("Error retrieving data from NASA API");
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new BadRequestException("An unexpected error occurred: " + e.getMessage());
        }
    }




    public ExceptionResponseDTO<String> saveTop10ClosestAsteroids(String startDate, String endDate) {
        log.info("Saving top 10 closest asteroids to the database for date range: {} to {}", startDate, endDate);
        try {
            TopAsteroidsResponseDTO topAsteroidsResponse = getTop10ClosestAsteroids(startDate, endDate);

            List<AsteroidEntity> asteroidEntities = topAsteroidsResponse.getAsteroids().stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList());

            asteroidRepository.saveAll(asteroidEntities);
            log.info("Successfully saved top 10 closest asteroids to the database");

            ExceptionResponseDTO<String> response = new ExceptionResponseDTO<>();
            response.setStatus(200);
            response.setMessage("Successfully saved top 10 closest asteroids to the database");
            response.setDetails("Your database is updated");

            return response;

        } catch (Exception e) {
            log.error("Error saving asteroids to the database: {}", e.getMessage());
            return new ExceptionResponseDTO<>(
                    500,
                    "Error saving asteroids to the database.",
                    "End date is maximum 7 days after start date."
            );
        }
    }


    public AsteroidDTO getAsteroidById(String id) {
        String url = String.format("%s/neo/rest/v1/neo/%s?api_key=%s", apiUrl, id, apiKey);
        try {
            ResponseEntity<AsteroidModel> response = restTemplate.getForEntity(url, AsteroidModel.class);

            if (response.getBody() == null) {
                throw new AsteroidNotFoundException("Asteroid with ID " + id + " not found");
            }

            AsteroidModel asteroidModel = response.getBody();
            return convertToDto(asteroidModel);
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
                .filter(asteroidModel -> asteroidModel.getClose_approach_data() != null && !asteroidModel.getClose_approach_data().isEmpty())
                .filter(asteroidModel -> {
                    CloseApproachData closestApproach = asteroidModel.getClose_approach_data().getFirst();
                    return Double.parseDouble(closestApproach.getMiss_distance().getKilometers()) >= minDistance;
                })
                .count();

        return new CountResponseDTO(count);
    }

    private AsteroidDTO convertToDto(AsteroidModel asteroidModel) {
        if (asteroidModel.getClose_approach_data() == null || asteroidModel.getClose_approach_data().isEmpty()) {
            throw new IllegalArgumentException("Asteroid does not have close approach data");
        }
        CloseApproachData closestApproach = asteroidModel.getClose_approach_data().getFirst();
        return new AsteroidDTO(
                asteroidModel.getId(),
                asteroidModel.getName(),
                asteroidModel.getAbsolute_magnitude_h(),
                asteroidModel.getEstimated_diameter().getKilometers().getEstimated_diameter_min(),
                asteroidModel.getEstimated_diameter().getKilometers().getEstimated_diameter_max(),
                Double.parseDouble(String.valueOf(closestApproach.getMiss_distance().getKilometers())),
                asteroidModel.is_potentially_hazardous_asteroid()
        );
    }

    private AsteroidEntity convertToEntity(AsteroidDTO dto) {
        AsteroidEntity asteroid = new AsteroidEntity();
        asteroid.setAsteroidId(String.valueOf(dto.getId()));
        asteroid.setName(dto.getName());
        asteroid.setAbsoluteMagnitude(dto.getAbsoluteMagnitude());
        asteroid.setEstimatedDiameterMin(dto.getEstimatedDiameterMin());
        asteroid.setEstimatedDiameterMax(dto.getEstimatedDiameterMax());
        asteroid.setMissDistanceKilometers(dto.getMissDistanceKilometers());
        asteroid.setPotentiallyHazardousAsteroid(dto.getIsPotentiallyHazardousAsteroid());
        return asteroid;
    }
}