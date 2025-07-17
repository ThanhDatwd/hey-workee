package com.katech.service.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class JsonConverter<T> implements AttributeConverter<List<T>, String> {

    private final ObjectMapper mapper;
    private final Class<T> type;

    @Override
    public String convertToDatabaseColumn(List<T> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Không thể convert List sang JSON", e);
        }
    }

    @Override
    public List<T> convertToEntityAttribute(String json) {
        try {
            if (json == null || json.trim().isEmpty()) {
                return Collections.emptyList();
            }
            //            return mapper.readValue(json, new TypeReference<List<T>>() {});
            return mapper.readValue(
                    json, mapper.getTypeFactory().constructCollectionType(List.class, type));
        } catch (IOException e) {
            throw new IllegalArgumentException("Không thể convert JSON sang List", e);
        }
    }
}
