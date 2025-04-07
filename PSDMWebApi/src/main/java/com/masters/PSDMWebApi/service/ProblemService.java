package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.model.Problem;
import java.util.List;
import java.util.Optional;

public interface ProblemService {
    List<Problem> getAllProblems();

    Optional<Problem> getProblemById(Long id);

    Problem createProblem(Problem session);

    Problem updateProblem(Long id, Problem session);

    void deleteProblem(Long id);
}
