package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.dto.SessionDetailsDTO;
import com.masters.PSDMWebApi.dto.request.CreateProblemAndSessionRequestDTO;
import com.masters.PSDMWebApi.model.Session;
import java.util.List;
import java.util.Optional;

public interface SessionService {

    Optional<Session> getSessionById(Long id);

    Optional<SessionDetailsDTO> getSessionDetailsById(Long id);

    Session createSession(Session session);

    Session createProblemAndSession(CreateProblemAndSessionRequestDTO dto);

    void addUsers(Long id, List<Long> usersIds);
}

