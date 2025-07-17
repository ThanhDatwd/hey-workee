package com.katech.service.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.katech.service.customer.dto.ResponseMetaData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class BaseResponse<T> {

    @JsonProperty("metadata")
    private ResponseMetaData metadata;

    @JsonProperty("data")
    private T data;

    @Override
    public String toString() {
        return "{" + "metadata: " + metadata + ", data: " + data + ", error: " + error + '}';
    }

    @JsonProperty("error")
    private Map<String, Object> error;
}
