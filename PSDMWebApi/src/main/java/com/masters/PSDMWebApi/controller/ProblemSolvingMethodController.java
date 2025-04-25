package com.masters.PSDMWebApi.controller;

import com.masters.PSDMWebApi.dto.ProblemSolvingMethodDTO;
import com.masters.PSDMWebApi.mapper.ProblemSolvingMethodMapper;
import com.masters.PSDMWebApi.model.ProblemSolvingMethod;
import com.masters.PSDMWebApi.service.ProblemSolvingMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/problemSolvingMethod")
@RequiredArgsConstructor
public class ProblemSolvingMethodController {

    private final ProblemSolvingMethodService problemSolvingMethodService;

    @GetMapping
    public List<ProblemSolvingMethodDTO> getAllProblemSolvingMethods() {
        return problemSolvingMethodService.getAllProblemSolvingMethods()
                .stream()
                .map(ProblemSolvingMethodMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/problem/{problemId}")
    public List<ProblemSolvingMethodDTO> getAllProblemSolvingMethodsByProblemId(@PathVariable Long problemId) {
        return problemSolvingMethodService.getAllProblemSolvingMethods()
                .stream()
                .map(ProblemSolvingMethodMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemSolvingMethodDTO> getProblemSolvingMethodById(@PathVariable Long id) {
        return problemSolvingMethodService.getProblemSolvingMethodById(id)
                .map(ProblemSolvingMethodMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProblemSolvingMethodDTO> createProblemSolvingMethod(@RequestBody ProblemSolvingMethodDTO dto) {
        ProblemSolvingMethod problemSolvingMethod = ProblemSolvingMethodMapper.toEntity(dto);
        ProblemSolvingMethod saved = problemSolvingMethodService.createProblemSolvingMethod(problemSolvingMethod);
        return ResponseEntity.ok(ProblemSolvingMethodMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProblemSolvingMethodDTO> updateProblemSolvingMethod(@PathVariable Long id, @RequestBody ProblemSolvingMethodDTO dto) {
        ProblemSolvingMethod problemSolvingMethodToUpdate = ProblemSolvingMethodMapper.toEntity(dto);
        ProblemSolvingMethod updated = problemSolvingMethodService.updateProblemSolvingMethod(id, problemSolvingMethodToUpdate);
        return ResponseEntity.ok(ProblemSolvingMethodMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblemSolvingMethod(@PathVariable Long id) {
        problemSolvingMethodService.deleteProblemSolvingMethod(id);
        return ResponseEntity.noContent().build();
    }
}

