package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.dto.request.GroupSolutionRequestDTO;
import com.masters.PSDMWebApi.model.Solution;

import java.util.List;
import java.util.Optional;

public interface SolutionService {
    List<Solution> getAllSolutions();

    Optional<Solution> getSolutionById(Long id);

    Solution createSolution(Solution solution);

    Solution groupSolutions(GroupSolutionRequestDTO dto);

    Solution updateSolution(Long id, Solution solution);

    void deleteSolution(Long id);

    List<Solution> getSolutionsBySessionId(Long sessionId);

    List<Solution> getSolutionsByParentSessionIdOrSessionId(Long sessionId);
}

