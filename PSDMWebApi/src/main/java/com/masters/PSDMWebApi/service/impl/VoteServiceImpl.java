package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.Vote;
import com.masters.PSDMWebApi.model.id.VoteId;
import com.masters.PSDMWebApi.repository.VoteRepository;
import com.masters.PSDMWebApi.service.VoteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    public VoteServiceImpl(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    @Override
    public Optional<Vote> getVoteById(VoteId id) {
        return voteRepository.findById(id);
    }

    @Override
    public Vote saveVote(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public void deleteVote(VoteId id) {
        voteRepository.deleteById(id);
    }
}
