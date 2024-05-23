package com.dwidi.nasaasteroidsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponseDTO <T> {
    private int status;
    private String message;
    private T details;
}
