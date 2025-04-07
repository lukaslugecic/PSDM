package com.masters.PSDMWebApi.mapper;

import com.masters.PSDMWebApi.dto.ProblemDTO;
import com.masters.PSDMWebApi.model.Problem;
import com.masters.PSDMWebApi.model.User;

public class ProblemMapper {

    public static ProblemDTO toDTO(Problem problem) {
        if (problem == null) return null;

        ProblemDTO dto = new ProblemDTO();
        dto.setId(problem.getId());
        dto.setTitle(problem.getProblemTitle());
        dto.setModeratorId(problem.getModerator().getId());
        dto.setStart(problem.getProblemStart());
        dto.setEnd(problem.getProblemEnd());
        return dto;
    }

    public static Problem toEntity(ProblemDTO dto) {
        if (dto == null) return null;

        Problem entity = new Problem();
        entity.setId(dto.getId());
        entity.setProblemTitle(dto.getTitle());
        entity.setModerator(new User(dto.getModeratorId()));
        entity.setProblemStart(dto.getStart());
        entity.setProblemEnd(dto.getEnd());
        return entity;
    }
}
