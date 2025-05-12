package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.Solution;
import com.masters.PSDMWebApi.model.User;
import com.masters.PSDMWebApi.model.Vote;
import com.masters.PSDMWebApi.repository.VoteRepository;
import com.masters.PSDMWebApi.service.SessionService;
import com.masters.PSDMWebApi.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final SessionService sessionService;

    @Override
    public void createVotes(List<Vote> votes) {
        voteRepository.saveAll(votes);
    }

    public boolean haveAllUsersVoted(Long sessionId) {
        List<Long> userIdsInSession = sessionService.getSessionById(sessionId)
                .map(session -> session.getUsers()
                        .stream()
                        .map(User::getId)
                        .toList())
                .orElseThrow(() -> new RuntimeException("Session not found"));

        List<Long> userIdsWhoVoted = findUserIdsBySessionId(sessionId);
        return new HashSet<>(userIdsInSession).containsAll(userIdsWhoVoted) && userIdsWhoVoted.size() == userIdsInSession.size();
    }
    // TODO vjv nebu delalo

    private List<Long> findUserIdsBySessionId(Long sessionId) {
        return sessionService.getSessionById(sessionId)
                .map(session -> session.getSolutions()
                        .stream()
                        .map(Solution::getUser)
                        .toList()
                        .stream()
                        .map(User::getId)
                        .toList())
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

}
