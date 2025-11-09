package com.masters.PSDMWebApi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemRequestDTO {
    private String title;
    private String description;
    private Long moderatorId;
}