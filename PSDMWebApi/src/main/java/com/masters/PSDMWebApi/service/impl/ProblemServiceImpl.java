package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.Problem;
import com.masters.PSDMWebApi.repository.ProblemRepository;
import com.masters.PSDMWebApi.service.ProblemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;

    @Override
    public List<Problem> getAllProblems() {
        return problemRepository.findAll();
    }

    @Override
    public Optional<Problem> getProblemById(Long id) {
        return problemRepository.findById(id);
    }

    @Override
    public Problem createProblem(Problem problem) {
        return problemRepository.save(problem);
    }

    @Override
    public Problem updateProblem(Long id, Problem problem) {
        problem.setId(id);
        return problemRepository.save(problem);
    }

    @Override
    public void deleteProblem(Long id) {
        problemRepository.deleteById(id);
    }
}
