package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.Step;
import com.masters.PSDMWebApi.repository.StepRepository;
import com.masters.PSDMWebApi.service.StepService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StepServiceImpl implements StepService {

    private final StepRepository sessionRepository;

    public StepServiceImpl(StepRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<Step> getAllSteps() {
        return sessionRepository.findAll();
    }

    @Override
    public Optional<Step> getStepById(Long id) {
        return sessionRepository.findById(id);
    }

    @Override
    public Step createStep(Step session) {
        return sessionRepository.save(session);
    }

    @Override
    public Step updateStep(Long id, Step session) {
        session.setId(id);
        return sessionRepository.save(session);
    }

    @Override
    public void deleteStep(Long id) {
        sessionRepository.deleteById(id);
    }
}
