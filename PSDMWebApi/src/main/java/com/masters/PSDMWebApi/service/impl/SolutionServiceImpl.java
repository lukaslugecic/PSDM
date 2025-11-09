package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.dto.SolutionScoreDTO;
import com.masters.PSDMWebApi.dto.request.GroupSolutionRequestDTO;
import com.masters.PSDMWebApi.mapper.SolutionMapper;
import com.masters.PSDMWebApi.model.Session;
import com.masters.PSDMWebApi.model.Solution;
import com.masters.PSDMWebApi.model.Vote;
import com.masters.PSDMWebApi.repository.SolutionRepository;
import com.masters.PSDMWebApi.service.AttributeService;
import com.masters.PSDMWebApi.service.SessionService;
import com.masters.PSDMWebApi.service.SolutionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SolutionServiceImpl implements SolutionService {

    private final SolutionRepository solutionRepository;
    private final SessionService sessionService;
    private final AttributeService attributeService;

    @Override
    @Transactional
    public Solution createSolution(Solution solution) {
        log.info("Saving solution with title: {}", solution.getTitle());
        return solutionRepository.save(solution);
    }

    @Override
    public List<Solution> getSolutionsBySessionId(Long id) {
        return solutionRepository.findBySessionId(id)
                .stream()
                .filter(solution -> solution.getGrouped().equals(false))
                .collect(Collectors.toList());
    }

    @Override
    public List<Solution> getSolutionsByParentSessionIdOrSessionId (Long id) {
        return solutionRepository.findBySessionIdOrParentSessionId(id)
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

    @Override
    @Transactional
    public List<SolutionScoreDTO> getBestSolutions(Long sessionId){
        Optional<Session> sessionOptional = sessionService.getSessionById(sessionId);
        if (sessionOptional.isEmpty()) {
            return Collections.emptyList();
        }
        Session session = sessionOptional.get();

        return switch (session.getDecisionMakingMethod().getTitle()) {
            case "Average winner" -> getAverageWinnerFinalSolution(session);
            case "Weighted average winner" -> getWeightedAverageWinnerFinalSolution(session);
            case "Borda ranking" -> getBordaRankingFinalSolution(session);
            case "Majority rule" -> getMajorityRuleFinalSolution(session);
            default -> throw new IllegalStateException("Unexpected value: " + session.getDecisionMakingMethod().getTitle());
        };
    }


    private List<SolutionScoreDTO> getAverageWinnerFinalSolution(Session session) {
        var scores = getSolutionsByParentSessionIdOrSessionId(
                session.getId())
                .stream()
                .filter(s -> s != null && s.getVotes() != null && !s.getVotes().isEmpty())
                .collect(Collectors.toMap(
                        Solution::getId,
                        s -> s.getVotes().stream()
                                .mapToDouble(Vote::getValue)
                                .average()
                                .orElse(0.0)
                ));


        Optional<Long> singleBestSolutionId = getUniqueMaxEntry(scores);
        singleBestSolutionId.ifPresent(this::chooseWinningSolution);
        log.info("Single best solution for average winner found: {}", singleBestSolutionId);

        return toScoredDTOList(scores);
    }


    private List<SolutionScoreDTO> getMajorityRuleFinalSolution(Session session) {
        List<Solution> solutions = getSolutionsByParentSessionIdOrSessionId(session.getId());

        Map<Long, Double> scores = solutions.stream()
                .filter(s -> s.getVotes() != null && !s.getVotes().isEmpty())
                .collect(Collectors.toMap(
                        Solution::getId,
                        s -> (double) s.getVotes().stream()
                                .filter(v -> v.getValue() == 1)
                                .count()
                ));

        int totalVotes = solutions.stream()
                .filter(s -> s.getVotes() != null)
                .mapToInt(s -> s.getVotes().size())
                .sum();

        double majorityThreshold = totalVotes / 2.0;

        Optional<Long> winningSolutionId = scores.entrySet().stream()
                .filter(entry -> entry.getValue() > majorityThreshold)
                .map(Map.Entry::getKey)
                .findFirst();

        winningSolutionId.ifPresent(this::chooseWinningSolution);
        log.info("Majority rule winner found: {}", winningSolutionId.orElse(null));

        return toScoredDTOList(scores);
    }



    private List<SolutionScoreDTO> getBordaRankingFinalSolution(Session session) {
        List<Solution> solutions = getSolutionsByParentSessionIdOrSessionId(session.getId());
        int totalSolutions = solutions.size();

        Map<Long, Double> scores = solutions.stream()
                .filter(s -> s != null && s.getVotes() != null && !s.getVotes().isEmpty())
                .collect(Collectors.toMap(
                        Solution::getId,
                        s -> s.getVotes().stream()
                                .mapToDouble(v -> {
                                    int rank = (int) Math.round(v.getValue());
                                    rank = Math.max(1, Math.min(rank, totalSolutions));
                                    return totalSolutions - rank + 1;
                                })
                                .sum()
                ));

        Optional<Long> singleBestSolutionId = getUniqueMaxEntry(scores);
        singleBestSolutionId.ifPresent(this::chooseWinningSolution);
        log.info("Single best solution for Borda ranking found: {}", singleBestSolutionId);

        return toScoredDTOList(scores);
    }

    private List<SolutionScoreDTO> getWeightedAverageWinnerFinalSolution(Session session) {
        Double sumOfWeights = getAllWeightsByParentSessionIdOrSessionId(session.getId())
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        var scores = getSolutionsByParentSessionIdOrSessionId(session.getId()).stream()
                .filter(s -> s != null && s.getVotes() != null && !s.getVotes().isEmpty())
                .collect(Collectors.toMap(
                        Solution::getId,
                        s -> weightedAverage(s, sumOfWeights)
                ));

        Optional<Long> singleBestSolutionId = getUniqueMaxEntry(scores);
        singleBestSolutionId.ifPresent(this::chooseWinningSolution);
        log.info("Single best solution for weighted average winner: {}", singleBestSolutionId);

        return toScoredDTOList(scores);
    }

    private List<Double> getAllWeightsByParentSessionIdOrSessionId(Long sessionId) {
        return getSolutionsByParentSessionIdOrSessionId(sessionId)
                .stream()
                .map(solution -> attributeService.getWeightBySolutionId(solution.getId()))
                .collect(Collectors.toList());
    }

    private double weightedAverage(Solution solution, Double sumOfWeights) {
        List<Vote> votes = solution.getVotes();
        double weight = attributeService.getWeightBySolutionId(solution.getId());

        double weightedSum = votes.stream()
                .mapToDouble(v -> v.getValue() * weight)
                .sum();
        return weightedSum / sumOfWeights;
    }

    private  List<SolutionScoreDTO> toScoredDTOList(Map<Long, Double> scores) {
        int BEST_SOLUTIONS_COUNT = 5;
        return scores.entrySet().stream()
                .map(entry -> new SolutionScoreDTO(
                        SolutionMapper.toDTO(solutionRepository
                                .findById(entry.getKey())
                                .orElseThrow(() -> new RuntimeException("Could not find solution"))),
                        entry.getValue()))
                .sorted(Comparator.comparing(SolutionScoreDTO::getScore).reversed())
                .limit(BEST_SOLUTIONS_COUNT)
                .toList();
    }


    private Optional<Long> getUniqueMaxEntry(Map<Long, Double> scores) {
        return scores.entrySet().stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue,
                        Collectors.mapping(
                                Map.Entry::getKey,
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .filter(e -> e.getValue().size() == 1)
                .map(e -> e.getValue().getFirst());
    }

    private void chooseWinningSolution(Long solutionId) {
        solutionRepository.findById(solutionId)
                .map(solution -> {
                    solution.setChosen(true);
                    return solutionRepository.save(solution);
                })
                .orElseThrow(() -> new RuntimeException("Solution not found"));
    }
}

