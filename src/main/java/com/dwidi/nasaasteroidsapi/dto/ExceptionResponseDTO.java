package com.dwidi.nasaasteroidsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponseDTO<T> {
    private Integer status;
    private String message;
    private T details;
}

