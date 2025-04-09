package com.masters.PSDMWebApi.controller;

import com.masters.PSDMWebApi.dto.SessionDTO;
import com.masters.PSDMWebApi.mapper.SessionMapper;
import com.masters.PSDMWebApi.model.Session;
import com.masters.PSDMWebApi.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    public List<SessionDTO> getAllSessions() {
        return sessionService.getAllSessions()
                .stream()
                .map(SessionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable Long id) {
        return sessionService.getSessionById(id)
                .map(SessionMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SessionDTO> createSession(@RequestBody SessionDTO dto) {
        Session session = SessionMapper.toEntity(dto);
        Session saved = sessionService.createSession(session);
        return ResponseEntity.ok(SessionMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessionDTO> updateSession(@PathVariable Long id, @RequestBody SessionDTO dto) {
        Session sessionToUpdate = SessionMapper.toEntity(dto);
        Session updated = sessionService.updateSession(id, sessionToUpdate);
        return ResponseEntity.ok(SessionMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}


