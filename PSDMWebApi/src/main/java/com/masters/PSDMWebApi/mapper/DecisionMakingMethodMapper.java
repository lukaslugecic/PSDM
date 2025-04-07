package com.masters.PSDMWebApi.mapper;

import com.masters.PSDMWebApi.dto.DecisionMakingMethodDTO;
import com.masters.PSDMWebApi.model.DecisionMakingMethod;

public class DecisionMakingMethodMapper {

    public static DecisionMakingMethodDTO toDTO(DecisionMakingMethod method) {
        if (method == null) return null;

        DecisionMakingMethodDTO dto = new DecisionMakingMethodDTO();
        dto.setId(method.getId());
        dto.setTitle(method.getDecisionMakingMethodTitle());
        dto.setDescription(method.getDecisionMakingMethodDescription());
        return dto;
    }

    public static DecisionMakingMethod toEntity(DecisionMakingMethodDTO dto) {
        if (dto == null) return null;

        DecisionMakingMethod entity = new DecisionMakingMethod();
        entity.setId(dto.getId());
        entity.setDecisionMakingMethodDescription(dto.getTitle());
        entity.setDecisionMakingMethodDescription(dto.getDescription());
        return entity;
    }
}

