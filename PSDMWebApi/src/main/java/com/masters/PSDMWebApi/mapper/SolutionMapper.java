package com.masters.PSDMWebApi.mapper;

import com.masters.PSDMWebApi.dto.AttributeDTO;
import com.masters.PSDMWebApi.dto.SolutionDTO;
import com.masters.PSDMWebApi.dto.request.SolutionRequestDTO;
import com.masters.PSDMWebApi.model.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class SolutionMapper {

    public static SolutionDTO toDTO(Solution solution) {
        if (solution == null) return null;

        List<AttributeDTO> attributeDTOs = null;
        if (solution.getAttributes() != null) {
            attributeDTOs = solution.getAttributes().stream()
                    .map(attr -> new AttributeDTO(
                            attr.getId(),
                            solution.getId(),
                            attr.getTitle(),
                            attr.getValue()
                    ))
                    .toList();
        }

        return new SolutionDTO(
                solution.getId(),
                solution.getTitle(),
                2L,
                solution.getProblem().getId(), // TODO fiksat
                solution.getSession().getId(),
                solution.getCreatedTime(),
                solution.getChosen(),
                solution.getGrouped(),
                attributeDTOs
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


        log.info("dto title: {}", dto.getTitle());

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

        log.info("Solution title in entity: {}", entity.getTitle());

        return entity;
    }
}
