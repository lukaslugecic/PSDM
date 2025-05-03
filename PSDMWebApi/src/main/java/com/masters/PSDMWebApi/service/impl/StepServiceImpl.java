package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.Step;
import com.masters.PSDMWebApi.repository.StepRepository;
import com.masters.PSDMWebApi.service.StepService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StepServiceImpl implements StepService {

    private final StepRepository stepRepository;

    public StepServiceImpl(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @Override
    public List<Step> getAllSteps() {
        return stepRepository.findAll();
    }

    @Override
    public Optional<Step> getStepById(Long id) {
        return stepRepository.findById(id);
    }

    @Override
    public Step createStep(Step step) {
        return stepRepository.save(step);
    }

    @Override
    public Step updateStep(Long id, Step step) {
        step.setId(id);
        return stepRepository.save(step);
    }

    @Override
    public void deleteStep(Long id) {
        stepRepository.deleteById(id);
    }
}
