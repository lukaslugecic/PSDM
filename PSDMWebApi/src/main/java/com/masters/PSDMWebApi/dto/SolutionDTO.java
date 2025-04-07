package com.masters.PSDMWebApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolutionDTO {
    private Long id;
    private String title;
    private String description;
    private Long userId;
    private Long problemId;
    private Long sessionId;
    private LocalDateTime createdTime;
    private Boolean chosen;
}
