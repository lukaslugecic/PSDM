package com.masters.PSDMWebApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepDetailsDTO {
    private Long id;
    private String title;
    private String description;
    private Integer ordinal;
    private Duration duration;
}