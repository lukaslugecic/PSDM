package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.dto.ProblemSessionDTO;
import com.masters.PSDMWebApi.dto.request.CreateProblemAndSessionRequestDTO;
import com.masters.PSDMWebApi.mapper.ProblemMapper;
import com.masters.PSDMWebApi.mapper.SessionMapper;
import com.masters.PSDMWebApi.model.*;
import com.masters.PSDMWebApi.repository.SessionRepository;
import com.masters.PSDMWebApi.service.*;
import com.masters.PSDMWebApi.util.Pair;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final ProblemService problemService;
    private final ProblemSolvingMethodService problemSolvingMethodService;
    private final UserService userService;

    @Override
    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    @Override
    public Optional<ProblemSessionDTO> getSessionDetailsById(Long id) {
        return sessionRepository.findById(id)
                .flatMap(session -> {
                        ProblemSessionDTO detailsDTO = new ProblemSessionDTO(
                                ProblemMapper.toDTO(session.getProblem()),
                                SessionMapper.toDTO(session),
                                session.getParentSession() != null ? session.getParentSession().getId() : null
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

        Optional<ProblemSolvingMethod> methodOpt =
                problemSolvingMethodService.getProblemSolvingMethodById(dto.getSession().getProblemSolvingMethodId());
        if (methodOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid problem solving method ID: " + dto.getSession().getProblemSolvingMethodId());
        }
        session.setProblemSolvingMethod(methodOpt.get());

        session.setEnd(calculateEndOfSession(session));
        return sessionRepository.save(session);
    }

    private LocalDateTime calculateEndOfSession(Session session) {
        switch (session.getProblemSolvingMethod().getTitle()) {
            case "Brainstorming" -> {
                return calculateEndOfBrainstorimingSession(session);
            }
            case "Brainwriting" -> {
                return calculateEndOfBrainwritingSession(session);
            }
            case "Nominal group technique" -> {
                return calculateEndOfNominalGroupSession(session);
            }
            case "Speedstorming" -> {
                return calculateEndOfSpeedstormingSession(session);
            }
            default -> throw new IllegalStateException("Unexpected value: " + session.getDecisionMakingMethod().getTitle());
        }
    }

    private LocalDateTime calculateEndOfBrainstorimingSession(Session session) {
        return session.getStart()
                .plusSeconds(session.getDuration().toSeconds());
               // .plusSeconds(getDurationInSecondsFromStepTitle(session,"Idea Clarification"));
    }

    private LocalDateTime calculateEndOfSpeedstormingSession(Session session) {
        LocalDateTime start = session.getStart();
        long x = session.getDuration().toSeconds();

        int n;
        if(session.getUsers() == null || session.getUsers().size() < 2) {
           return start;
        } else {
            n = session.getUsers().size();
        }

        int totalPairs = n * (n - 1) / 2;
        int maxPairsPerRound = n / 2;
        int rounds = (int) Math.ceil((double) totalPairs / maxPairsPerRound);

        return start.plusSeconds(rounds * x);
    }


    private LocalDateTime calculateEndOfBrainwritingSession(Session session) {
        if(session.getUsers() == null || session.getUsers().size() < 2) {
            return session.getStart();
        }
        return session.getStart()
                .plusSeconds(session.getDuration().toSeconds() * session.getUsers().size());
    }

    private LocalDateTime calculateEndOfNominalGroupSession(Session session) {
        return session.getStart()
                .plusSeconds(session.getDuration().toSeconds());
              //  .plusSeconds(getDurationInSecondsFromStepTitle(session,"Idea Clarification"));
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

        addUsersToSession(session, usersToAdd);

        session.setEnd(calculateEndOfSession(session));

        handleSubsessions(session);

        sessionRepository.save(session);
    }

    @Override
    public List<Session> getAllSessionsByProblemId(Long problemId) {
        return sessionRepository.findByProblemId(problemId);
    }

    private void addUsersToSession(Session session, List<User> users) {
        if(session.getUsers() == null) {
            session.setUsers(new ArrayList<>(users));
        } else {
            session.getUsers().clear();
            session.getUsers().addAll(users);
        }
        sessionRepository.save(session);
    }

    private void handleSubsessions(Session session) {
        switch (session.getProblemSolvingMethod().getTitle()) {
            case "Brainstorming" -> {}
            case "Brainwriting" -> handleBrainwritingSubsessions(session);
            case "Nominal group technique" -> handleNominalGroupTechniqueSubsessions(session);
            case "Speedstorming" -> handleSpeedstormingSubsessions(session);
            default -> throw new IllegalStateException("Unexpected value: " + session.getDecisionMakingMethod().getTitle());
        }
    }

    private void handleNominalGroupTechniqueSubsessions(Session session) {

        for (User user : session.getUsers()) {
            Session subsession = createSubSession(session, session.getStart(), session.getDuration().toSeconds());
            addUsersToSession(subsession, List.of(user));
        }

        // TODO handle finished subsessions
    }

    private void handleSpeedstormingSubsessions(Session session) {
        List<User> participants = session.getUsers();
        long duration = session.getDuration().toSeconds();
        if (participants == null || participants.size() < 2) return;

        List<List<Pair<User, User>>> slots = getSlots(participants);

        LocalDateTime start = session.getStart();
        for (int round = 0; round < slots.size(); round++) {
            List<Pair<User, User>> slot = slots.get(round);
            LocalDateTime slotStartTime = start.plusSeconds(round * duration);

            log.info("ROUND {} — START: {}", round + 1, slotStartTime);
            for (Pair<User, User> pair : slot) {
                log.info("Pair: {} {}", pair.key().getFirstName(), pair.value().getLastName());

                Session subsession = createSubSession(session, slotStartTime, duration);
                addUsersToSession(subsession, List.of(pair.key(), pair.value()));
            }
        }
    }

    private static List<List<Pair<User, User>>> getSlots(List<User> participants) {
        List<Pair<User, User>> allPairs = new ArrayList<>();

        for (int i = 0; i < participants.size(); i++) {
            for (int j = i + 1; j < participants.size(); j++) {
                allPairs.add(new Pair<>(participants.get(i), participants.get(j)));
            }
        }

        List<List<Pair<User, User>>> slots = new ArrayList<>();
        while (!allPairs.isEmpty()) {
            List<Pair<User, User>> currentSlot = new ArrayList<>();
            Set<User> used = new HashSet<>();
            Iterator<Pair<User, User>> iterator = allPairs.iterator();

            while (iterator.hasNext()) {
                Pair<User, User> pair = iterator.next();
                User u1 = pair.key();
                User u2 = pair.value();

                if (!used.contains(u1) && !used.contains(u2)) {
                    currentSlot.add(pair);
                    used.add(u1);
                    used.add(u2);
                    iterator.remove();
                }
            }
            slots.add(currentSlot);
        }
        return slots;
    }

    private void handleBrainwritingSubsessions(Session session) {

        List<User> participants = session.getUsers();
        List<Session> subsessions = new ArrayList<>();

        long duration = session.getDuration().toSeconds();

        log.info("Got duration: {}", duration);

        for (User user : participants) {
            Session subsession = createSubSession(session, session.getStart(), participants.size() * duration);
            subsessions.add(subsession);
            addUsersToSession(subsession, List.of(user));
        }

        int totalRounds = participants.size() - 1;

        for (int round = 1; round <= totalRounds; round++) {
            int finalRound = round;
            scheduler.schedule(() -> {
                for (int i = 0; i < participants.size(); i++) {
                    User user = participants.get(i);
                    int targetIndex = (i + finalRound) % participants.size();
                    Session targetSubsession = subsessions.get(targetIndex);
                    synchronized (targetSubsession) {
                        addUsersToSession(targetSubsession, List.of(user));
                    }
                }
                System.out.println("Round " + finalRound + " completed.");
            }, duration * round, TimeUnit.SECONDS);
        }
    }


    private Session createSubSession(Session parentSession, LocalDateTime start, Long duration) {
        log.info("creating new subsession for: {}", parentSession);
        Session subSession = new Session();
        subSession.setStart(start);
        subSession.setEnd(start.plusSeconds(duration));
        subSession.setDuration(parentSession.getDuration());
        subSession.setAttributes(parentSession.getAttributes());
        subSession.setDecisionMakingMethod(parentSession.getDecisionMakingMethod());
        subSession.setProblemSolvingMethod(parentSession.getProblemSolvingMethod());
        subSession.setProblem(parentSession.getProblem());
        subSession.setParentSession(parentSession);
        return sessionRepository.save(subSession);
    }

}
