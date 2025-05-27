package com.masters.PSDMWebApi.mapper;

import com.masters.PSDMWebApi.dto.SessionDTO;
import com.masters.PSDMWebApi.dto.SessionDetailsDTO;
import com.masters.PSDMWebApi.dto.request.SessionRequestDTO;
import com.masters.PSDMWebApi.model.DecisionMakingMethod;
import com.masters.PSDMWebApi.model.Problem;
import com.masters.PSDMWebApi.model.ProblemSolvingMethod;
import com.masters.PSDMWebApi.model.Session;
import com.masters.PSDMWebApi.util.AttributeConverter;

import java.time.Duration;
import java.time.LocalDateTime;

public class SessionMapper {

    public static SessionDTO toDTO(Session session) {
        if (session == null) return null;

        SessionDTO dto = new SessionDTO();
        dto.setId(session.getId());
        dto.setProblemId(session.getProblem().getId());
        dto.setProblemSolvingMethodId(session.getProblemSolvingMethod().getId());
        dto.setDecisionMakingMethodId(session.getDecisionMakingMethod().getId());
        dto.setDuration(session.getDuration().toSeconds());
        dto.setAttributes(session.getAttributes());
        dto.setStart(session.getStart());
        dto.setEnd(session.getEnd());
        if(session.getParentSession() != null){
            dto.setParentSessionId(session.getParentSession().getId());
        }
        return dto;
    }

    public static SessionDetailsDTO toDetailsDTO(Session session) {
        if (session == null) return null;

        SessionDetailsDTO dto = new SessionDetailsDTO();
        dto.setId(session.getId());
        dto.setStart(session.getStart());
        dto.setEnd(session.getEnd());
        dto.setProblemSolvingMethod(session.getProblemSolvingMethod().getTitle());
        dto.setDecisionMakingMethod(session.getDecisionMakingMethod().getTitle());
        dto.setAttributes(AttributeConverter.decodeAttributes(session.getAttributes()));
        return dto;
    }

    public static Session toEntity(SessionDTO dto) {
        if (dto == null) return null;

        Session entity = new Session();
        entity.setId(dto.getId());
        entity.setProblem(new Problem(dto.getProblemId()));
        entity.setProblemSolvingMethod(new ProblemSolvingMethod(dto.getProblemSolvingMethodId()));
        entity.setDecisionMakingMethod(new DecisionMakingMethod(dto.getDecisionMakingMethodId()));
        entity.setDuration(Duration.ofSeconds(dto.getDuration()));
        entity.setAttributes(dto.getAttributes());
        entity.setStart(dto.getStart());
        entity.setEnd(dto.getEnd());
        if(dto.getParentSessionId() != null){
            entity.setParentSession(new Session(dto.getParentSessionId()));
        }
        return entity;
    }

    public static Session toEntity(SessionRequestDTO dto) {
        if (dto == null) return null;

        Session entity = new Session();
        entity.setProblem(new Problem(dto.getProblemId()));
        entity.setProblemSolvingMethod(new ProblemSolvingMethod(dto.getProblemSolvingMethodId()));
        entity.setDecisionMakingMethod(new DecisionMakingMethod(dto.getDecisionMakingMethodId()));
        entity.setDuration(Duration.ofSeconds(dto.getDuration()));
        entity.setAttributes(dto.getAttributes());
        entity.setStart(LocalDateTime.now());
        return entity;
    }
}

