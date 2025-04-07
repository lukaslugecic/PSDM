package com.masters.PSDMWebApi.mapper;

import com.masters.PSDMWebApi.dto.SessionUsersDTO;
import com.masters.PSDMWebApi.model.Session;
import com.masters.PSDMWebApi.model.SessionUsers;
import com.masters.PSDMWebApi.model.User;

public class SessionUsersMapper {

    public static SessionUsersDTO toDTO(SessionUsers sessionUsers) {
        if (sessionUsers == null) return null;

        SessionUsersDTO dto = new SessionUsersDTO();
        dto.setSessionId(sessionUsers.getSession().getId());
        dto.setUserId(sessionUsers.getUser().getId());
        return dto;
    }

    public static SessionUsers toEntity(SessionUsersDTO dto) {
        if (dto == null) return null;

        SessionUsers entity = new SessionUsers();
        entity.setSession(new Session(dto.getSessionId()));
        entity.setUser(new User(dto.getUserId()));
        return entity;
    }
}
