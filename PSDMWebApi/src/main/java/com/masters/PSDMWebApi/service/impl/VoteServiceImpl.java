package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.Session;
import com.masters.PSDMWebApi.model.User;
import com.masters.PSDMWebApi.model.Vote;
import com.masters.PSDMWebApi.repository.VoteRepository;
import com.masters.PSDMWebApi.service.SessionService;
import com.masters.PSDMWebApi.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final SessionService sessionService;

    @Override
    public void createVotes(List<Vote> votes) {
        voteRepository.saveAll(votes);
    }

    @Override
    public boolean haveAllUsersVoted(Long sessionId) {
        Session session = sessionService.getSessionById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        List<User> sessionUsers = session.getUsers();

        Set<User> usersWhoVoted = session.getSolutions().stream()
                .flatMap(solution -> solution.getVotes().stream())
                .map(Vote::getUser)
                .collect(Collectors.toSet());

        return usersWhoVoted.containsAll(sessionUsers);
    }
}
