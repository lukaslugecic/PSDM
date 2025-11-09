package com.masters.PSDMWebApi.controller;

import com.masters.PSDMWebApi.dto.ProblemDTO;
import com.masters.PSDMWebApi.dto.ProblemDetailsDTO;
import com.masters.PSDMWebApi.mapper.ProblemMapper;
import com.masters.PSDMWebApi.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/problem")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/{id}")
    public ResponseEntity<ProblemDTO> getProblemById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ProblemMapper.toDTO(
                        problemService.getProblemById(id)
                )
        );
    }

    @GetMapping("/with-solutions/user/{userId}")
    public List<ProblemDetailsDTO> getAllProblemsWithSolutionByUserId(@PathVariable Long userId) {
        return problemService.getAllProblemsWithSolutionByUserId(userId);
    }
}

