package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.Problem;
import com.masters.PSDMWebApi.repository.ProblemRepository;
import com.masters.PSDMWebApi.service.ProblemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository sessionRepository;

    public ProblemServiceImpl(ProblemRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<Problem> getAllProblems() {
        return sessionRepository.findAll();
    }

    @Override
    public Optional<Problem> getProblemById(Long id) {
        return sessionRepository.findById(id);
    }

    @Override
    public Problem createProblem(Problem session) {
        return sessionRepository.save(session);
    }

    @Override
    public Problem updateProblem(Long id, Problem session) {
        session.setId(id);
        return sessionRepository.save(session);
    }

    @Override
    public void deleteProblem(Long id) {
        sessionRepository.deleteById(id);
    }
}
