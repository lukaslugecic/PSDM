package com.masters.PSDMWebApi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProblemAndSessionRequestDTO {
    private ProblemRequestDTO problem;
    private SessionRequestDTO session;
}
