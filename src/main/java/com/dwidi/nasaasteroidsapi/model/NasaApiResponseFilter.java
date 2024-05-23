package com.dwidi.nasaasteroidsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NasaApiResponseFilter {
    private Map<String, String> links;
    private List<Asteroid> near_earth_objects;
}
