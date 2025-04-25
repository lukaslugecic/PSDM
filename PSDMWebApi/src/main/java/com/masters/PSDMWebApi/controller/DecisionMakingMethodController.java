package com.masters.PSDMWebApi.controller;

import com.masters.PSDMWebApi.dto.DecisionMakingMethodDTO;
import com.masters.PSDMWebApi.mapper.DecisionMakingMethodMapper;
import com.masters.PSDMWebApi.model.DecisionMakingMethod;
import com.masters.PSDMWebApi.service.DecisionMakingMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/decisionMakingMethod")
@RequiredArgsConstructor
public class DecisionMakingMethodController {

    private final DecisionMakingMethodService decisonMakingMethodService;

    @GetMapping
    public List<DecisionMakingMethodDTO> getAllDecisionMakingMethods() {
        return decisonMakingMethodService.getAllDecisionMakingMethods()
                .stream()
                .map(DecisionMakingMethodMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/problem/{problemId}")
    public List<DecisionMakingMethodDTO> getAllDecisionMakingMethodsByProblemId(@PathVariable Long problemId) {
        return decisonMakingMethodService.getAllDecisionMakingMethods()
                .stream()
                .map(DecisionMakingMethodMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DecisionMakingMethodDTO> getDecisionMakingMethodById(@PathVariable Long id) {
        return decisonMakingMethodService.getDecisionMakingMethodById(id)
                .map(DecisionMakingMethodMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DecisionMakingMethodDTO> createDecisionMakingMethod(@RequestBody DecisionMakingMethodDTO dto) {
        DecisionMakingMethod decisonMakingMethod = DecisionMakingMethodMapper.toEntity(dto);
        DecisionMakingMethod saved = decisonMakingMethodService.createDecisionMakingMethod(decisonMakingMethod);
        return ResponseEntity.ok(DecisionMakingMethodMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DecisionMakingMethodDTO> updateDecisionMakingMethod(@PathVariable Long id, @RequestBody DecisionMakingMethodDTO dto) {
        DecisionMakingMethod decisonMakingMethodToUpdate = DecisionMakingMethodMapper.toEntity(dto);
        DecisionMakingMethod updated = decisonMakingMethodService.updateDecisionMakingMethod(id, decisonMakingMethodToUpdate);
        return ResponseEntity.ok(DecisionMakingMethodMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDecisionMakingMethod(@PathVariable Long id) {
        decisonMakingMethodService.deleteDecisionMakingMethod(id);
        return ResponseEntity.noContent().build();
    }
}

