package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.DecisionMakingMethod;
import com.masters.PSDMWebApi.repository.DecisionMakingMethodRepository;
import com.masters.PSDMWebApi.service.DecisionMakingMethodService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DecisionMakingMethodServiceImpl implements DecisionMakingMethodService {

    private final DecisionMakingMethodRepository sessionRepository;

    public DecisionMakingMethodServiceImpl(DecisionMakingMethodRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<DecisionMakingMethod> getAllDecisionMakingMethods() {
        return sessionRepository.findAll();
    }

    @Override
    public Optional<DecisionMakingMethod> getDecisionMakingMethodById(Long id) {
        return sessionRepository.findById(id);
    }

    @Override
    public DecisionMakingMethod createDecisionMakingMethod(DecisionMakingMethod session) {
        return sessionRepository.save(session);
    }

    @Override
    public DecisionMakingMethod updateDecisionMakingMethod(Long id, DecisionMakingMethod session) {
        session.setId(id);
        return sessionRepository.save(session);
    }

    @Override
    public void deleteDecisionMakingMethod(Long id) {
        sessionRepository.deleteById(id);
    }
}
