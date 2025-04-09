package com.masters.PSDMWebApi.mapper;


import com.masters.PSDMWebApi.dto.AttributeDTO;
import com.masters.PSDMWebApi.model.Attribute;
import com.masters.PSDMWebApi.model.Solution;

public class AttributeMapper {

    public static AttributeDTO toDTO(Attribute attribute) {
        if (attribute == null) return null;

        AttributeDTO dto = new AttributeDTO();
        dto.setId(attribute.getId());
        dto.setSolutionId(attribute.getSolution().getId());
        dto.setTitle(attribute.getTitle());
        return dto;
    }

    public static Attribute toEntity(AttributeDTO dto) {
        if (dto == null) return null;

        Attribute entity = new Attribute();
        entity.setId(dto.getId());
        entity.setSolution(new Solution(dto.getSolutionId()));
        entity.setTitle(dto.getTitle());
        return entity;
    }
}
