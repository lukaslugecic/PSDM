package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.model.Vote;

import java.util.List;

public interface VoteService {
    void createVotes(List<Vote> votes);

    boolean haveAllUsersVoted(Long sessionId);
}
