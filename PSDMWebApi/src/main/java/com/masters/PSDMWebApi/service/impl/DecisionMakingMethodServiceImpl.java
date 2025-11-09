package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.DecisionMakingMethod;
import com.masters.PSDMWebApi.repository.DecisionMakingMethodRepository;
import com.masters.PSDMWebApi.service.DecisionMakingMethodService;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
