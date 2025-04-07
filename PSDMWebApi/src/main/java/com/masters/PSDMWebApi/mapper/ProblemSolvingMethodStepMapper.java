package com.masters.PSDMWebApi.mapper;


import com.masters.PSDMWebApi.dto.ProblemSolvingMethodStepDTO;
import com.masters.PSDMWebApi.model.ProblemSolvingMethod;
import com.masters.PSDMWebApi.model.ProblemSolvingMethodStep;
import com.masters.PSDMWebApi.model.Step;

public class ProblemSolvingMethodStepMapper {

    public static ProblemSolvingMethodStepDTO toDTO(ProblemSolvingMethodStep methodStep) {
        if (methodStep == null) return null;

        ProblemSolvingMethodStepDTO dto = new ProblemSolvingMethodStepDTO();
        dto.setMethodId(methodStep.getMethod().getId());
        dto.setStepId(methodStep.getStep().getId());
        dto.setOrdinal(methodStep.getOrdinal());
        dto.setRepetitions(methodStep.getRepetitions());
        dto.setDuration(methodStep.getDuration());
        return dto;
    }

    public static ProblemSolvingMethodStep toEntity(ProblemSolvingMethodStepDTO dto) {
        if (dto == null) return null;

        ProblemSolvingMethodStep entity = new ProblemSolvingMethodStep();
        entity.setMethod(new ProblemSolvingMethod(dto.getMethodId()));
        entity.setStep(new Step(dto.getStepId()));
        entity.setOrdinal(dto.getOrdinal());
        entity.setRepetitions(dto.getRepetitions());
        entity.setDuration(dto.getDuration());
        return entity;
    }
}

