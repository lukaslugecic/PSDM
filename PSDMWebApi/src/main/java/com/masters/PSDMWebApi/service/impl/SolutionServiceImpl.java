package com.masters.PSDMWebApi.service.impl;

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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SolutionServiceImpl implements SolutionService {

    private final SolutionRepository solutionRepository;
    private final SessionService sessionService;
    private final AttributeService attributeService;

    @Override
    public List<Solution> getAllSolutions() {
        return solutionRepository.findAll();
    }

    @Override
    public Optional<Solution> getSolutionById(Long id) {
        return solutionRepository.findById(id);
    }

    @Override
    @Transactional
    public Solution createSolution(Solution solution) {
        log.info("Saving solution with title: {}", solution.getTitle());
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
    public Solution getWinnigSolution(Long sessionId){
        return solutionRepository.findById(Objects.requireNonNull(getBestSolution(sessionId)))
                .orElse(null);
    }


    private Long getBestSolution(Long id) {
        Optional<Session> sessionOptional = sessionService.getSessionById(id);
        if (sessionOptional.isEmpty()) {
            return null;
        }
        Session session = sessionOptional.get();

        return switch (session.getDecisionMakingMethod().getTitle()) {
            case "Average winner" -> getAverageWinnerFinalSolution(session).orElse(null);
            case "Weighted average winner" -> getWeightedAverageWinnerFinalSolution(session).orElse(null);
            case "Borda ranking" -> getBordaRankingFinalSolution(session).orElse(null);
            case "Majority rule" -> getMajorityRuleFinalSolution(session).orElse(null);
            default -> throw new IllegalStateException("Unexpected value: " + session.getDecisionMakingMethod().getTitle());
        };
    }


    private Optional<Long> getAverageWinnerFinalSolution(Session session) {
        var scores = getSolutionsByParentSessionIdOrSessionId(session.getId()).stream()
                .filter(s -> s != null && s.getVotes() != null && !s.getVotes().isEmpty())
                .collect(Collectors.toMap(
                        Solution::getId,
                        s -> s.getVotes().stream()
                                .mapToDouble(Vote::getValue)
                                .average()
                                .orElse(0.0)
                ));

        log.info("Calculated scores for average winner: {}", scores);
        return getUniqueMaxEntry(scores); // TODO nije potrebno jer ni u jednom slucaju vise njih ne moze imati iznad thresholdas
    }


    private Optional<Long> getMajorityRuleFinalSolution(Session session) {
        List<Solution> solutions = getSolutionsByParentSessionIdOrSessionId(session.getId());
        // TODO check if voter voted for only one solution

        Map<Long, Long> scores = solutions.stream()
                .filter(s -> s.getVotes() != null && !s.getVotes().isEmpty())
                .collect(Collectors.toMap(
                        Solution::getId,
                        s -> s.getVotes().stream()
                                .filter(v -> v.getValue() == 1)
                                .count()
                ));

        long totalVotes = scores.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        long majorityThreshold = totalVotes / 2 + 1;

        Map<Long, Long> scoresOverThreshold = scores.entrySet().stream()
                .filter(entry -> entry.getValue() >= majorityThreshold)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        log.info("Calculated scores for majority threshold: {}", scoresOverThreshold);
        return getUniqueMaxEntry(scoresOverThreshold);
    }


    private Optional<Long> getBordaRankingFinalSolution(Session session) {
        List<Solution> solutions = getSolutionsByParentSessionIdOrSessionId(session.getId());
        int totalSolutions = solutions.size();

        Map<Long, Integer> scores = solutions.stream()
                .filter(s -> s != null && s.getVotes() != null && !s.getVotes().isEmpty())
                .collect(Collectors.toMap(
                        Solution::getId,
                        s -> s.getVotes().stream()
                                .mapToInt(v -> {
                                    int rank = (int) Math.round(v.getValue());
                                    rank = Math.max(1, Math.min(rank, totalSolutions));
                                    return totalSolutions - rank + 1;
                                })
                                .sum()
                ));

        log.info("Calculated scores for borda ranking: {}", scores);
        return getUniqueMaxEntry(scores);
    }

    private Optional<Long> getWeightedAverageWinnerFinalSolution(Session session) {

        Double sumOfWeights = attributeService.getAllWeightsBySessionId(session.getId())
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        var scores = session.getSolutions().stream()
                .filter(s -> s != null && s.getVotes() != null && !s.getVotes().isEmpty())
                .collect(Collectors.toMap(
                        Solution::getId,
                        s -> weightedAverage(s, sumOfWeights)
                ));

        log.info("Calculated scores for weighted average: {}", scores);
        return getUniqueMaxEntry(scores);
    }

    private double weightedAverage(Solution solution, Double sumOfWeights) {
        List<Vote> votes = solution.getVotes();
        double weight = attributeService.getWeightBySolutionId(solution.getId());

        double weightedSum = votes.stream()
                .mapToDouble(v -> v.getValue() * weight)
                .sum();

        return weightedSum / sumOfWeights;
    }



    private <K, V extends Comparable<V>> Optional<K> getUniqueMaxEntry(Map<K, V> scores) {
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
                .map(e -> {
                            log.info("Max Entry: {}", e.getKey());
                            return e.getValue().getFirst();
                        }
                );
    }
}

