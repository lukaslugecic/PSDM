package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.dto.request.GroupSolutionRequestDTO;
import com.masters.PSDMWebApi.mapper.SolutionMapper;
import com.masters.PSDMWebApi.model.Solution;
import com.masters.PSDMWebApi.repository.SolutionRepository;
import com.masters.PSDMWebApi.service.SolutionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SolutionServiceImpl implements SolutionService {

    private final SolutionRepository solutionRepository;

    public SolutionServiceImpl(SolutionRepository solutionRepository) {
        this.solutionRepository = solutionRepository;
    }

    @Override
    public List<Solution> getAllSolutions() {
        return solutionRepository.findAll();
    }

    @Override
    public Optional<Solution> getSolutionById(Long id) {
        return solutionRepository.findById(id);
    }

    @Override
    public Solution createSolution(Solution solution) {
        return solutionRepository.save(solution);
    }

    @Override
    public Solution updateSolution(Long id, Solution solution) {
        solution.setId(id);
        return solutionRepository.save(solution);
    }

    @Override
    public void deleteSolution(Long id) {
        solutionRepository.deleteById(id);
    }

    @Override
    public List<Solution> getSolutionsBySessionId(Long id) {
        return solutionRepository.findBySessionId(id)
                .stream()
                .filter(solution -> solution.getGrouped().equals(false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Solution groupSolutions(GroupSolutionRequestDTO dto) {
        List<Solution> solutions = solutionRepository.findAllById(dto.getSolutionIds());

        solutions.forEach(solution -> solution.setGrouped(true));
        solutionRepository.saveAll(solutions);

        return createSolution(SolutionMapper.toEntity(dto.getSolution()));
    }

}

