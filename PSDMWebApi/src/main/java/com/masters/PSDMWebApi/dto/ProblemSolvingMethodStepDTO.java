package com.masters.PSDMWebApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemSolvingMethodStepDTO {
    private Long methodId;
    private Long stepId;
    private Integer ordinal;
    private Integer repetitions;
    private Duration duration;
}
