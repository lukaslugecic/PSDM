package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.model.Session;
import java.util.List;
import java.util.Optional;

public interface SessionService {
    List<Session> getAllSessions();

    Optional<Session> getSessionById(Long id);

    Session createSession(Session session);

    Session updateSession(Long id, Session session);

    void deleteSession(Long id);
}

