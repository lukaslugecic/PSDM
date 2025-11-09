package com.masters.PSDMWebApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProblemSessionDTO {
    private ProblemDTO problem;
    private SessionDTO session;
    private Long parentSessionId;
}
