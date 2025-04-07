package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.ProblemSolvingMethodStep;
import com.masters.PSDMWebApi.model.id.ProblemSolvingMethodStepId;
import com.masters.PSDMWebApi.repository.ProblemSolvingMethodStepRepository;
import com.masters.PSDMWebApi.service.ProblemSolvingMethodStepService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemSolvingMethodStepServiceImpl implements ProblemSolvingMethodStepService {

    private final ProblemSolvingMethodStepRepository sessionRepository;

    public ProblemSolvingMethodStepServiceImpl(ProblemSolvingMethodStepRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<ProblemSolvingMethodStep> getAllProblemSolvingMethodSteps() {
        return sessionRepository.findAll();
    }

    @Override
    public Optional<ProblemSolvingMethodStep> getProblemSolvingMethodStepById(ProblemSolvingMethodStepId id) {
        return sessionRepository.findById(id);
    }

    @Override
    public ProblemSolvingMethodStep createProblemSolvingMethodStep(ProblemSolvingMethodStep session) {
        return sessionRepository.save(session);
    }

    @Override
    public ProblemSolvingMethodStep updateProblemSolvingMethodStep(ProblemSolvingMethodStepId id, ProblemSolvingMethodStep session) {
        return sessionRepository.save(session);
    }

    @Override
    public void deleteProblemSolvingMethodStep(ProblemSolvingMethodStepId id) {
        sessionRepository.deleteById(id);
    }
}
