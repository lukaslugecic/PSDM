package com.masters.PSDMWebApi.controller;

import com.masters.PSDMWebApi.dto.DecisionMakingMethodDTO;
import com.masters.PSDMWebApi.mapper.DecisionMakingMethodMapper;
import com.masters.PSDMWebApi.service.DecisionMakingMethodService;
import lombok.RequiredArgsConstructor;
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
}

