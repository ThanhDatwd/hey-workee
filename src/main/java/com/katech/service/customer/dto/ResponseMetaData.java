package com.katech.service.customer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class ResponseMetaData {

    @JsonProperty private int httpCode;

    @JsonProperty private String path;

    @JsonProperty private String message;

    @JsonProperty private String responseTraceId;
    @JsonProperty private String sessionId;
}
