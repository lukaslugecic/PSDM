package com.masters.PSDMWebApi.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDetailsDTO {
    private ProblemDTO problem;
    private SolutionDTO solution;
}
