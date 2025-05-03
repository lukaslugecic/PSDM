package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.dto.StepDetailsDTO;
import com.masters.PSDMWebApi.model.ProblemSolvingMethodStep;
import com.masters.PSDMWebApi.model.id.ProblemSolvingMethodStepId;
import com.masters.PSDMWebApi.repository.ProblemSolvingMethodStepRepository;
import com.masters.PSDMWebApi.service.ProblemSolvingMethodStepService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemSolvingMethodStepServiceImpl implements ProblemSolvingMethodStepService {

    private final ProblemSolvingMethodStepRepository methodStepRepository;

    public ProblemSolvingMethodStepServiceImpl(ProblemSolvingMethodStepRepository methodStepRepository) {
        this.methodStepRepository = methodStepRepository;
    }

    @Override
    public List<ProblemSolvingMethodStep> getAllProblemSolvingMethodSteps() {
        return methodStepRepository.findAll();
    }

    @Override
    public Optional<ProblemSolvingMethodStep> getProblemSolvingMethodStepById(ProblemSolvingMethodStepId id) {
        return methodStepRepository.findById(id);
    }

    @Override
    public ProblemSolvingMethodStep createProblemSolvingMethodStep(ProblemSolvingMethodStep methodStep) {
        return methodStepRepository.save(methodStep);
    }

    @Override
    public ProblemSolvingMethodStep updateProblemSolvingMethodStep(ProblemSolvingMethodStepId id, ProblemSolvingMethodStep methodStep) {
        return methodStepRepository.save(methodStep);
    }

    @Override
    public void deleteProblemSolvingMethodStep(ProblemSolvingMethodStepId id) {
        methodStepRepository.deleteById(id);
    }

    @Override
    public List<StepDetailsDTO> getStepDetails (List<ProblemSolvingMethodStep> methodSteps) {
        List<StepDetailsDTO> stepDetails = new ArrayList<>();

        for (ProblemSolvingMethodStep methodStep : methodSteps) {
            StepDetailsDTO stepDetail = new StepDetailsDTO();

            stepDetail.setId(methodStep.getStep().getId());
            stepDetail.setTitle(methodStep.getStep().getTitle());
            stepDetail.setDescription(methodStep.getStep().getDescription());

            stepDetail.setOrdinal(methodStep.getOrdinal());
            stepDetail.setDuration(methodStep.getDuration());
            stepDetails.add(stepDetail);
        }

        return stepDetails;
    }
}
