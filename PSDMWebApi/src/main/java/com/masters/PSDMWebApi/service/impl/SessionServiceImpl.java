package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.Session;
import com.masters.PSDMWebApi.repository.SessionRepository;
import com.masters.PSDMWebApi.service.SessionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    @Override
    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    @Override
    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    @Override
    public Session updateSession(Long id, Session session) {
        session.setId(id);
        return sessionRepository.save(session);
    }

    @Override
    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }
}
