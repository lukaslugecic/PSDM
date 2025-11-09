package com.masters.PSDMWebApi.repository;

import com.masters.PSDMWebApi.model.ProblemSolvingMethodStep;
import com.masters.PSDMWebApi.model.id.ProblemSolvingMethodStepId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemSolvingMethodStepRepository extends JpaRepository<ProblemSolvingMethodStep, ProblemSolvingMethodStepId> {
}
