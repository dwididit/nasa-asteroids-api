package com.dwidi.nasaasteroidsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopAsteroidsResponseDTO {
    private List<AsteroidDTO> asteroids;
    private Integer status;
    private String detail;
}
