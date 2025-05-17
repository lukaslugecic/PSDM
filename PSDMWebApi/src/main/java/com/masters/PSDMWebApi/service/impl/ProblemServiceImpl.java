package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.dto.ProblemDetailsDTO;
import com.masters.PSDMWebApi.mapper.ProblemMapper;
import com.masters.PSDMWebApi.mapper.SolutionMapper;
import com.masters.PSDMWebApi.model.Problem;
import com.masters.PSDMWebApi.repository.ProblemRepository;
import com.masters.PSDMWebApi.service.ProblemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;

    @Override
    public List<ProblemDetailsDTO> getAllProblemsWithSolutionByUserId(Long userId) {
        List<Problem> problems = problemRepository.findByModeratorId(userId);

        return problems.stream().map(
                problem -> new ProblemDetailsDTO(
                        ProblemMapper.toDTO(problem),
                        SolutionMapper.toDTO(problem.getSolutions().stream()
                                .filter(solution -> solution.getChosen().equals(true))
                                .findFirst().orElse(null)
                        )
                )
        ).toList();
    }

    @Override
    public Problem createProblem(Problem problem) {
        return problemRepository.save(problem);
    }
}
