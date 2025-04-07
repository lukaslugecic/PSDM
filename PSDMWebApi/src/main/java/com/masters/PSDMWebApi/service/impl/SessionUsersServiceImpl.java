package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.SessionUsers;
import com.masters.PSDMWebApi.model.id.SessionUsersId;
import com.masters.PSDMWebApi.repository.SessionUsersRepository;
import com.masters.PSDMWebApi.service.SessionUsersService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionUsersServiceImpl implements SessionUsersService {

    private final SessionUsersRepository voteRepository;

    public SessionUsersServiceImpl(SessionUsersRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public List<SessionUsers> getAllSessionUserss() {
        return voteRepository.findAll();
    }

    @Override
    public Optional<SessionUsers> getSessionUsersById(SessionUsersId id) {
        return voteRepository.findById(id);
    }

    @Override
    public SessionUsers saveSessionUsers(SessionUsers vote) {
        return voteRepository.save(vote);
    }

    @Override
    public void deleteSessionUsers(SessionUsersId id) {
        voteRepository.deleteById(id);
    }
}
