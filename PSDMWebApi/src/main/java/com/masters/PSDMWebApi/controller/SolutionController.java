package com.masters.PSDMWebApi.controller;

import com.masters.PSDMWebApi.dto.SolutionDTO;
import com.masters.PSDMWebApi.mapper.SolutionMapper;
import com.masters.PSDMWebApi.model.Solution;
import com.masters.PSDMWebApi.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/solution")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;

    @GetMapping
    public List<SolutionDTO> getAllSolutions() {
        return solutionService.getAllSolutions()
                .stream()
                .map(SolutionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/problem/{problemId}")
    public List<SolutionDTO> getAllSolutionsByProblemId(@PathVariable Long problemId) {
        return solutionService.getAllSolutions()
                .stream()
                .map(SolutionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolutionDTO> getSolutionById(@PathVariable Long id) {
        return solutionService.getSolutionById(id)
                .map(SolutionMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SolutionDTO> createSolution(@RequestBody SolutionDTO dto) {
        Solution solution = SolutionMapper.toEntity(dto);
        Solution saved = solutionService.createSolution(solution);
        return ResponseEntity.ok(SolutionMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolutionDTO> updateSolution(@PathVariable Long id, @RequestBody SolutionDTO dto) {
        Solution solutionToUpdate = SolutionMapper.toEntity(dto);
        Solution updated = solutionService.updateSolution(id, solutionToUpdate);
        return ResponseEntity.ok(SolutionMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolution(@PathVariable Long id) {
        solutionService.deleteSolution(id);
        return ResponseEntity.noContent().build();
    }
}

