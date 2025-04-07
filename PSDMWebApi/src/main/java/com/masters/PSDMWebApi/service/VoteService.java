package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.model.Vote;
import com.masters.PSDMWebApi.model.id.VoteId;
import java.util.List;
import java.util.Optional;

public interface VoteService {
    List<Vote> getAllVotes();

    Optional<Vote> getVoteById(VoteId id);

    Vote saveVote(Vote vote);

    void deleteVote(VoteId id);
}
