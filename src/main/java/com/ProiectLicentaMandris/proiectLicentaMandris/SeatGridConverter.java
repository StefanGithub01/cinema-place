package com.ProiectLicentaMandris.proiectLicentaMandris;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Converter
public class SeatGridConverter implements AttributeConverter<Boolean[][], String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Boolean[][] attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting seatGrid to JSON", e);
        }
    }

    @Override
    public Boolean[][] convertToEntityAttribute(String dbData) {
        try {
            // Read the JSON as a 2D array of Booleans
            return objectMapper.readValue(dbData, Boolean[][].class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to seatGrid", e);
        }
    }
}

