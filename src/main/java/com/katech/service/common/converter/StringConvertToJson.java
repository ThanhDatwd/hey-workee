package com.katech.service.common.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StringConvertToJson extends JsonConverter<String> {
    public StringConvertToJson(ObjectMapper mapper) {
        super(mapper, String.class);
    }
}
