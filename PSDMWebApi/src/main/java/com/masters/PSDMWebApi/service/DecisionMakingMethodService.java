package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.model.DecisionMakingMethod;
import java.util.List;
import java.util.Optional;

public interface DecisionMakingMethodService {
    List<DecisionMakingMethod> getAllDecisionMakingMethods();

    Optional<DecisionMakingMethod> getDecisionMakingMethodById(Long id);

    DecisionMakingMethod createDecisionMakingMethod(DecisionMakingMethod session);

    DecisionMakingMethod updateDecisionMakingMethod(Long id, DecisionMakingMethod session);

    void deleteDecisionMakingMethod(Long id);
}
