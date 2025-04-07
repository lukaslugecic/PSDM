package com.masters.PSDMWebApi.controller;

import com.masters.PSDMWebApi.dto.ProblemDTO;
import com.masters.PSDMWebApi.mapper.ProblemMapper;
import com.masters.PSDMWebApi.model.Problem;
import com.masters.PSDMWebApi.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    public List<ProblemDTO> getAllProblems() {
        return problemService.getAllProblems()
                .stream()
                .map(ProblemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemDTO> getProblemById(@PathVariable Long id) {
        return problemService.getProblemById(id)
                .map(ProblemMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProblemDTO> createProblem(@RequestBody ProblemDTO dto) {
        Problem problem = ProblemMapper.toEntity(dto);
        Problem saved = problemService.createProblem(problem);
        return ResponseEntity.ok(ProblemMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProblemDTO> updateProblem(@PathVariable Long id, @RequestBody ProblemDTO dto) {
        Problem problemToUpdate = ProblemMapper.toEntity(dto);
        Problem updated = problemService.updateProblem(id, problemToUpdate);
        return ResponseEntity.ok(ProblemMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build();
    }
}

