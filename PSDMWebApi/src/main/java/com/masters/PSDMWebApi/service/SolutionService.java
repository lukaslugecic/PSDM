package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.dto.SolutionScoreDTO;
import com.masters.PSDMWebApi.dto.request.GroupSolutionRequestDTO;
import com.masters.PSDMWebApi.model.Solution;

import java.util.List;

public interface SolutionService {

    Solution createSolution(Solution solution);

    Solution groupSolutions(GroupSolutionRequestDTO dto);

    List<Solution> getSolutionsBySessionId(Long sessionId);

    List<Solution> getSolutionsByParentSessionIdOrSessionId(Long sessionId);

    List<SolutionScoreDTO> getBestSolutions(Long sessionId);
}

