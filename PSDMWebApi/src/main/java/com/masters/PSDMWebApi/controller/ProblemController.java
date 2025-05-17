package com.masters.PSDMWebApi.controller;

import com.masters.PSDMWebApi.dto.ProblemDetailsDTO;
import com.masters.PSDMWebApi.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/problem")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/with-solutions/user/{userId}")
    public List<ProblemDetailsDTO> getAllProblemsWithSolutionByUserId(@PathVariable Long userId) {
        return problemService.getAllProblemsWithSolutionByUserId(userId);
    }
}

