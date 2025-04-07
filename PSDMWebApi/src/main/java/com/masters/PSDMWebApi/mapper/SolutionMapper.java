package com.masters.PSDMWebApi.mapper;

import com.masters.PSDMWebApi.dto.SolutionDTO;
import com.masters.PSDMWebApi.model.Problem;
import com.masters.PSDMWebApi.model.Session;
import com.masters.PSDMWebApi.model.Solution;
import com.masters.PSDMWebApi.model.User;

public class SolutionMapper {

    public static SolutionDTO toDTO(Solution solution) {
        if (solution == null) return null;

        SolutionDTO dto = new SolutionDTO();
        dto.setId(solution.getId());
        dto.setTitle(solution.getSolutionTitle());
        dto.setDescription(solution.getSolutionDescription());
        dto.setUserId(solution.getUser().getId());
        dto.setProblemId(solution.getProblem().getId());
        dto.setSessionId(solution.getSession().getId());
        dto.setCreatedTime(solution.getCreatedTime());
        dto.setChosen(solution.getChosen());
        return dto;
    }

    public static Solution toEntity(SolutionDTO dto) {
        if (dto == null) return null;

        Solution entity = new Solution();
        entity.setId(dto.getId());
        entity.setSolutionTitle(dto.getTitle());
        entity.setSolutionDescription(dto.getDescription());
        entity.setUser(new User(dto.getUserId()));
        entity.setProblem(new Problem(dto.getProblemId()));
        entity.setSession(new Session(dto.getSessionId()));
        entity.setCreatedTime(dto.getCreatedTime());
        entity.setChosen(dto.getChosen());
        return entity;
    }
}
