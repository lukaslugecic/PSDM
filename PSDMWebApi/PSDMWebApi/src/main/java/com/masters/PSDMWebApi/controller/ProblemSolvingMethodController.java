package com.masters.PSDMWebApi.controller;

import com.masters.PSDMWebApi.dto.ProblemSolvingMethodDTO;
import com.masters.PSDMWebApi.mapper.ProblemSolvingMethodMapper;
import com.masters.PSDMWebApi.service.ProblemSolvingMethodService;
import lombok.RequiredArgsConstructor;
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
}

