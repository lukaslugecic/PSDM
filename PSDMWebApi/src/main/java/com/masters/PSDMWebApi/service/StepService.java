package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.model.Step;
import java.util.List;
import java.util.Optional;

public interface StepService {
    List<Step> getAllSteps();

    Optional<Step> getStepById(Long id);

    Step createStep(Step solution);

    Step updateStep(Long id, Step solution);

    void deleteStep(Long id);
}

