package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.dto.SessionDetailsDTO;
import com.masters.PSDMWebApi.dto.request.CreateProblemAndSessionRequestDTO;
import com.masters.PSDMWebApi.mapper.ProblemMapper;
import com.masters.PSDMWebApi.mapper.SessionMapper;
import com.masters.PSDMWebApi.model.*;
import com.masters.PSDMWebApi.repository.SessionRepository;
import com.masters.PSDMWebApi.service.AttributeService;
import com.masters.PSDMWebApi.service.ProblemService;
import com.masters.PSDMWebApi.service.SessionService;
import com.masters.PSDMWebApi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    private final AttributeService attributeService;
    private final ProblemService problemService;
    private final UserService userService;

    @Override
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    @Override
    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    @Override
    public Optional<SessionDetailsDTO> getSessionDetailsById(Long id) {
        return sessionRepository.findById(id)
                .flatMap(session -> {
                        SessionDetailsDTO detailsDTO = new SessionDetailsDTO(
                                ProblemMapper.toDTO(session.getProblem()),
                                SessionMapper.toDTO(session)
                                );
                        return Optional.of(detailsDTO);
                });
    }

    @Override
    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    @Override
    @Transactional
    public Session createProblemAndSession(CreateProblemAndSessionRequestDTO dto) {
        Problem problem = ProblemMapper.toEntity(dto.getProblem());
        problemService.createProblem(problem);
        Session session = SessionMapper.toEntity(dto.getSession());
        session.setProblem(problem);
        return sessionRepository.save(session);
    }

    @Override
    public Session updateSession(Long id, Session session) {
        session.setId(id);
        return sessionRepository.save(session);
    }

    @Override
    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addUsers(Long sessionId, List<Long> userIds) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + sessionId));

        List<User> usersToAdd = userIds.stream()
                .map(userService::getUserById)
                .flatMap(Optional::stream)
                .toList();

        session.getUsers().clear();
        session.getUsers().addAll(usersToAdd);
        sessionRepository.save(session);
    }


    @Override
    public Long getBestSolution(Long id) {
        Optional<Session> sessionOptional = sessionRepository.findById(id);
        if (sessionOptional.isEmpty()) {
            return null;
        }
        Session session = sessionOptional.get();
        return switch (session.getDecisionMakingMethod().getTitle()) {
            case "Average winner" -> getAverageWinnerFinalSolution(session).orElse(null);
            case "Weighted average winner" -> getWeightedAverageWinnerFinalSolution(session).orElse(null);
            case "Borda ranking" -> getBordaRankingFinalSolution(session).orElse(null);
            case "Majority rule" -> getMajorityRuleFinalSolution(session).orElse(null);
            default -> 5L;
        };
    }


    private Optional<Long> getAverageWinnerFinalSolution(Session session) {
        var scores = session.getSolutions().stream()
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
        List<Solution> solutions = session.getSolutions();
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
        List<Solution> solutions = session.getSolutions();
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
