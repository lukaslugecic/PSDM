package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.dto.StepDetailsDTO;
import com.masters.PSDMWebApi.model.ProblemSolvingMethodStep;
import com.masters.PSDMWebApi.model.id.ProblemSolvingMethodStepId;

import java.util.List;
import java.util.Optional;

public interface ProblemSolvingMethodStepService {
    List<ProblemSolvingMethodStep> getAllProblemSolvingMethodSteps();

    Optional<ProblemSolvingMethodStep> getProblemSolvingMethodStepById(ProblemSolvingMethodStepId id);

    ProblemSolvingMethodStep createProblemSolvingMethodStep(ProblemSolvingMethodStep session);

    ProblemSolvingMethodStep updateProblemSolvingMethodStep(ProblemSolvingMethodStepId id, ProblemSolvingMethodStep session);

    void deleteProblemSolvingMethodStep(ProblemSolvingMethodStepId id);

    List<StepDetailsDTO> getStepDetails (List<ProblemSolvingMethodStep> methodSteps);
}
