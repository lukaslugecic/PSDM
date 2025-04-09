package com.masters.PSDMWebApi.mapper;

import com.masters.PSDMWebApi.dto.SessionDTO;
import com.masters.PSDMWebApi.model.DecisionMakingMethod;
import com.masters.PSDMWebApi.model.ProblemSolvingMethod;
import com.masters.PSDMWebApi.model.Session;

public class SessionMapper {

    public static SessionDTO toDTO(Session session) {
        if (session == null) return null;

        SessionDTO dto = new SessionDTO();
        dto.setId(session.getId());
        dto.setProblemId(session.getProblem().getId());
        dto.setProblemSolvingMethodId(session.getProblemSolvingMethod().getId());
        dto.setDecisionMakingMethodId(session.getDecisionMakingMethod().getId());
        dto.setStart(session.getStart());
        dto.setEnd(session.getEnd());
        return dto;
    }

    public static Session toEntity(SessionDTO dto) {
        if (dto == null) return null;

        Session entity = new Session();
        entity.setId(dto.getId());
        entity.setId(dto.getProblemId());
        entity.setProblemSolvingMethod(new ProblemSolvingMethod(dto.getProblemSolvingMethodId()));
        entity.setDecisionMakingMethod(new DecisionMakingMethod(dto.getDecisionMakingMethodId()));
        entity.setStart(dto.getStart());
        entity.setEnd(dto.getEnd());
        return entity;
    }
}

