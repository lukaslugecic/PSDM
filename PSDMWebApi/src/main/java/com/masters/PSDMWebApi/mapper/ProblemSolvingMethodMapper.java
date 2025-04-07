package com.masters.PSDMWebApi.mapper;


import com.masters.PSDMWebApi.dto.ProblemSolvingMethodDTO;
import com.masters.PSDMWebApi.model.ProblemSolvingMethod;

public class ProblemSolvingMethodMapper {

    public static ProblemSolvingMethodDTO toDTO(ProblemSolvingMethod method) {
        if (method == null) return null;

        ProblemSolvingMethodDTO dto = new ProblemSolvingMethodDTO();
        dto.setId(method.getId());
        dto.setTitle(method.getProblemSolvingMethodTitle());
        dto.setDescription(method.getProblemSolvingMethodDescription());
        return dto;
    }

    public static ProblemSolvingMethod toEntity(ProblemSolvingMethodDTO dto) {
        if (dto == null) return null;

        ProblemSolvingMethod entity = new ProblemSolvingMethod();
        entity.setId(dto.getId());
        entity.setProblemSolvingMethodTitle(dto.getTitle());
        entity.setProblemSolvingMethodDescription(dto.getDescription());
        return entity;
    }
}

