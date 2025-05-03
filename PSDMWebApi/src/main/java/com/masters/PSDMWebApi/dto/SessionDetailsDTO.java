package com.masters.PSDMWebApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SessionDetailsDTO {
    private ProblemDTO problem;
    private SessionDTO session;
    private List<StepDetailsDTO> steps;
}
