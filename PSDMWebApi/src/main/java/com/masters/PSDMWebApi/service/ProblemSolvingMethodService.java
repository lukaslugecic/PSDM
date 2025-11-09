package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.model.ProblemSolvingMethod;
import java.util.List;
import java.util.Optional;

public interface ProblemSolvingMethodService {
    List<ProblemSolvingMethod> getAllProblemSolvingMethods();

    Optional<ProblemSolvingMethod> getProblemSolvingMethodById(Long id);
}
