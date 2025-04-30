package com.masters.PSDMWebApi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolutionRequestDTO {
    private String title;
    private String description;
    private Long userId;
    private Long problemId;
    private Long sessionId;
}
