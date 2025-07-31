package com.katech.service.quote.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDto {
    private String id;
    private String name;
    private String avatarUrl;
    private Double locationLat;
    private Double locationLng;
    private Double ratingAvg;
    private Integer jobsDone;
    private String address;
    private Boolean verified;
}
