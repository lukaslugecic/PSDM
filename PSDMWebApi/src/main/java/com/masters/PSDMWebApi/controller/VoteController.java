package com.masters.PSDMWebApi.controller;

import com.masters.PSDMWebApi.dto.request.VoteRequestDTO;
import com.masters.PSDMWebApi.mapper.VoteMapper;
import com.masters.PSDMWebApi.model.Vote;
import com.masters.PSDMWebApi.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
@Slf4j
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody List<VoteRequestDTO> dtos) {
        List<Vote> votes = dtos.stream()
                .map(VoteMapper::toEntity)
                .toList();

        voteService.createVotes(votes);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/hasEveryoneVoted/{sessionId}")
    public ResponseEntity<Boolean> hasEveryoneVoted(@PathVariable Long sessionId) {
        boolean allVoted = voteService.haveAllUsersVoted(sessionId);
        return ResponseEntity.ok(allVoted);
    }

}

