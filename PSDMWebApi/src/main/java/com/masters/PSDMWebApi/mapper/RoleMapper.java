package com.masters.PSDMWebApi.mapper;

import com.masters.PSDMWebApi.dto.RoleDTO;
import com.masters.PSDMWebApi.model.Role;

public class RoleMapper {

    public static RoleDTO toDTO(Role role) {
        if (role == null) return null;

        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setTitle(role.getTitle());
        return dto;
    }

    public static Role toEntity(RoleDTO dto) {
        if (dto == null) return null;

        Role entity = new Role();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        return entity;
    }
}
