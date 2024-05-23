package com.dwidi.nasaasteroidsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AsteroidResponseDTO {
    private List<AsteroidDTO> asteroid;
}
