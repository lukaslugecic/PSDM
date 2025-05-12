package com.masters.PSDMWebApi.mapper;


import com.masters.PSDMWebApi.dto.VoteDTO;
import com.masters.PSDMWebApi.dto.request.VoteRequestDTO;
import com.masters.PSDMWebApi.model.Solution;
import com.masters.PSDMWebApi.model.User;
import com.masters.PSDMWebApi.model.Vote;

import java.time.LocalDateTime;

public class VoteMapper {

    public static VoteDTO toDTO(Vote vote) {
        if (vote == null) return null;

        VoteDTO dto = new VoteDTO();
        dto.setUserId(vote.getUser().getId());
        dto.setSolutionId(vote.getSolution().getId());
        dto.setValue(vote.getValue());
        dto.setVotingTime(vote.getVotingTime());
        return dto;
    }

    public static Vote toEntity(VoteDTO dto) {
        if (dto == null) return null;

        Vote entity = new Vote();
        entity.setUser(new User(dto.getUserId()));
        entity.setSolution(new Solution(dto.getSolutionId()));
        entity.setValue(dto.getValue());
        entity.setVotingTime(dto.getVotingTime());
        return entity;
    }

    public static Vote toEntity(VoteRequestDTO dto) {
        if (dto == null) return null;

        Vote entity = new Vote();
        entity.setUser(new User(dto.getUserId()));
        entity.setSolution(new Solution(dto.getSolutionId()));
        entity.setValue(dto.getValue());
        entity.setVotingTime(LocalDateTime.now());
        return entity;
    }
}
