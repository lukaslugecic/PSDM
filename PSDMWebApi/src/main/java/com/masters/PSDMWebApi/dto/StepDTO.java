package com.masters.PSDMWebApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepDTO {
    private Long id;
    private String title;
    private String description;
}