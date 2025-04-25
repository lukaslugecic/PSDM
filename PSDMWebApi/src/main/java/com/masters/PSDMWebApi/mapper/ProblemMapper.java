package com.masters.PSDMWebApi.mapper;

import com.masters.PSDMWebApi.dto.ProblemDTO;
import com.masters.PSDMWebApi.dto.request.ProblemRequestDTO;
import com.masters.PSDMWebApi.model.Problem;
import com.masters.PSDMWebApi.model.User;

public class ProblemMapper {

    public static ProblemDTO toDTO(Problem problem) {
        if (problem == null) return null;

        ProblemDTO dto = new ProblemDTO();
        dto.setId(problem.getId());
        dto.setTitle(problem.getTitle());
        dto.setDescription(problem.getDescription());
        dto.setModeratorId(problem.getModerator().getId());
        dto.setStart(problem.getStart());
        dto.setEnd(problem.getEnd());
        return dto;
    }

    public static Problem toEntity(ProblemDTO dto) {
        if (dto == null) return null;

        Problem entity = new Problem();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setModerator(new User(dto.getModeratorId()));
        entity.setStart(dto.getStart());
        entity.setEnd(dto.getEnd());
        return entity;
    }

    public static Problem toEntity(ProblemRequestDTO dto) {
        if (dto == null) return null;

        Problem entity = new Problem();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setModerator(new User(dto.getModeratorId()));
        entity.setStart(dto.getStart());
        entity.setEnd(dto.getEnd());
        return entity;
    }
}
