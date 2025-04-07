package com.masters.PSDMWebApi.mapper;

import com.masters.PSDMWebApi.dto.StepDTO;
import com.masters.PSDMWebApi.model.Step;

public class StepMapper {

    public static StepDTO toDTO(Step step) {
        if (step == null) return null;

        StepDTO dto = new StepDTO();
        dto.setId(step.getId());
        dto.setTitle(step.getStepTitle());
        dto.setDescription(step.getStepDescription());
        return dto;
    }

    public static Step toEntity(StepDTO dto) {
        if (dto == null) return null;

        Step entity = new Step();
        entity.setId(dto.getId());
        entity.setStepTitle(dto.getTitle());
        entity.setStepDescription(dto.getDescription());
        return entity;
    }
}

