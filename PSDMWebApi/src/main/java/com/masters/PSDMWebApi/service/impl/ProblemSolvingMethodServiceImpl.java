package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.ProblemSolvingMethod;
import com.masters.PSDMWebApi.repository.ProblemSolvingMethodRepository;
import com.masters.PSDMWebApi.service.ProblemSolvingMethodService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemSolvingMethodServiceImpl implements ProblemSolvingMethodService {

    private final ProblemSolvingMethodRepository sessionRepository;

    public ProblemSolvingMethodServiceImpl(ProblemSolvingMethodRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<ProblemSolvingMethod> getAllProblemSolvingMethods() {
        return sessionRepository.findAll();
    }

    @Override
    public Optional<ProblemSolvingMethod> getProblemSolvingMethodById(Long id) {
        return sessionRepository.findById(id);
    }
}
