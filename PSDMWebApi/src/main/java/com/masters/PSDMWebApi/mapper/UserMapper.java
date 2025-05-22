package com.masters.PSDMWebApi.mapper;

import com.masters.PSDMWebApi.dto.UserDTO;
import com.masters.PSDMWebApi.model.Role;
import com.masters.PSDMWebApi.model.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setKeycloakId(user.getKeycloakId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setRoleId(user.getRole().getId());
        return dto;
    }

    public static User toEntity(UserDTO dto) {
        if (dto == null) return null;

        User entity = new User();
        entity.setId(dto.getId());
        entity.setKeycloakId(dto.getKeycloakId());
        entity.setUsername(dto.getUsername());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setRole(new Role(dto.getRoleId()));
        return entity;
    }
}

