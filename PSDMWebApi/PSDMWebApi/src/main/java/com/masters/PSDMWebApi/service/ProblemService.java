package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.dto.ProblemDetailsDTO;
import com.masters.PSDMWebApi.model.Problem;

import java.util.List;

public interface ProblemService {
    Problem getProblemById(Long id);

    void createProblem(Problem session);

    List<ProblemDetailsDTO> getAllProblemsWithSolutionByUserId(Long id);
}
