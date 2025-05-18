package com.masters.PSDMWebApi.controller;

import com.masters.PSDMWebApi.dto.SessionDTO;
import com.masters.PSDMWebApi.dto.SessionDetailsDTO;
import com.masters.PSDMWebApi.dto.request.CreateProblemAndSessionRequestDTO;
import com.masters.PSDMWebApi.dto.request.InviteUsersRequestDTO;
import com.masters.PSDMWebApi.dto.request.SessionRequestDTO;
import com.masters.PSDMWebApi.mapper.SessionMapper;
import com.masters.PSDMWebApi.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
@Slf4j
public class SessionController {

    private final SessionService sessionService;

    @GetMapping("/details/{id}")
    public ResponseEntity<SessionDetailsDTO> getSessionDetailsById(@PathVariable Long id) {
        return sessionService.getSessionDetailsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/andProblem")
    public ResponseEntity<SessionDTO> createProblemAndSession(@RequestBody CreateProblemAndSessionRequestDTO dto) {
        return ResponseEntity.ok(
                SessionMapper.toDTO(
                        sessionService.createProblemAndSession(dto)
                )
        );
    }


    @PostMapping()
    public ResponseEntity<SessionDTO> createSession(@RequestBody SessionRequestDTO dto) {
        log.warn("Create session request: {}", dto);

        return ResponseEntity.ok(
                SessionMapper.toDTO(
                        sessionService.createSession(SessionMapper.toEntity(dto))
                )
        );
    }

    @PostMapping("addUsers")
    public ResponseEntity<Void> addUsers(@RequestBody InviteUsersRequestDTO dto) {
        sessionService.addUsers(dto.getSessionId(), dto.getUserIds());
        return ResponseEntity.ok().build();
    }

}


