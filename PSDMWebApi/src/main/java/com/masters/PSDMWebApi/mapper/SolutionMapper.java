package com.masters.PSDMWebApi.mapper;

import com.masters.PSDMWebApi.dto.AttributeDTO;
import com.masters.PSDMWebApi.dto.SolutionDTO;
import com.masters.PSDMWebApi.dto.SolutionDetailsDTO;
import com.masters.PSDMWebApi.dto.request.SolutionRequestDTO;
import com.masters.PSDMWebApi.model.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class SolutionMapper {

    public static SolutionDTO toDTO(Solution solution) {
        if (solution == null) return null;
        return new SolutionDTO(
                solution.getId(),
                solution.getTitle(),
                solution.getUser().getId(),
                solution.getProblem().getId(),
                solution.getSession().getId(),
                solution.getCreatedTime(),
                solution.getChosen(),
                solution.getGrouped(),
                getAttributesForSolution(solution)
        );
    }

    public static SolutionDetailsDTO toDetailsDTO(Solution solution) {
        if (solution == null) return null;
        return new SolutionDetailsDTO(
                solution.getId(),
                solution.getTitle(),
                solution.getUser().getFirstName(),
                solution.getUser().getLastName(),
                getAttributesForSolution(solution)
        );
    }

    public static Solution toEntity(SolutionDTO dto) {
        if (dto == null) return null;

        Solution entity = new Solution();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setUser(new User(dto.getUserId()));
        entity.setProblem(new Problem(dto.getProblemId()));
        entity.setSession(new Session(dto.getSessionId()));
        entity.setCreatedTime(dto.getCreatedTime());
        entity.setChosen(dto.getChosen());
        entity.setGrouped(dto.getGrouped());
        return entity;
    }

    public static Solution toEntity(SolutionRequestDTO dto) {
        if (dto == null) return null;

        Solution entity = new Solution();
        entity.setTitle(dto.getTitle());
        entity.setUser(new User(dto.getUserId()));
        entity.setProblem(new Problem(dto.getProblemId()));
        entity.setSession(new Session(dto.getSessionId()));
        entity.setCreatedTime(LocalDateTime.now());
        entity.setChosen(false);
        entity.setGrouped(false);

        if(dto.getAttributes() != null) {
            List<Attribute> attributeList = dto.getAttributes().stream()
                    .map(atrDto -> {
                        Attribute atr = AttributeMapper.toEntity(atrDto);
                        atr.setSolution(entity);
                        return atr;
                    }).toList();

            entity.setAttributes(attributeList);
        }

        return entity;
    }


    private static List<AttributeDTO> getAttributesForSolution(Solution solution) {
        if (solution.getAttributes() != null) {
            return solution.getAttributes().stream()
                    .map(attr -> new AttributeDTO(
                            attr.getId(),
                            solution.getId(),
                            attr.getTitle(),
                            attr.getValue()
                    ))
                    .toList();
        }
        return null;
    }
}
