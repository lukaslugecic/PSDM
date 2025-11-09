package com.masters.PSDMWebApi.service.impl;

import com.masters.PSDMWebApi.model.Role;
import com.masters.PSDMWebApi.model.Session;
import com.masters.PSDMWebApi.model.User;
import com.masters.PSDMWebApi.repository.UserRepository;
import com.masters.PSDMWebApi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId);
    }

    @Override
    public Long getCurrentSessionId(Long userId, boolean isSubSession) {
        Long id = userRepository.findById(userId)
                .map(User::getSessions)
                .flatMap(sessions -> sessions.stream()
                        .filter(session -> isSubSession == (session.getParentSession() != null))
                        .filter(session -> session.getStart().isBefore(LocalDateTime.now())
                                && session.getEnd().isAfter(LocalDateTime.now()))
                        .findFirst()
                )
                .map(Session::getId)
                .orElse(null);

        log.warn("Found active session for user ID: {} {} and it is {}", userId, isSubSession, id);

        return id;
    }

    @Override
    public String getCurrentSessionUrl(Long userId) {
        Session currentSession = userRepository.findById(userId)
                .map(User::getSessions)
                .flatMap(sessions -> sessions.stream()
                        .filter(session -> session.getStart().isBefore(LocalDateTime.now())
                                && session.getEnd().isAfter(LocalDateTime.now()))
                        .findFirst()
                )
                .orElse(null);

        if (currentSession != null) {
            Long problemId = currentSession.getProblem().getId();
            Long sessionId = currentSession.getId();
            String attributes = currentSession.getAttributes();
            String rez = problemId + "/" + sessionId + "/" + attributes;
            log.error(rez);
            return rez;
        }

        return null;
    }

    @Override
    public Boolean checkParentSession(Long userId) {
        return userRepository.findById(userId)
                .map(User::getSessions)
                .flatMap(sessions -> sessions.stream()
                        .filter(session -> (session.getParentSession() == null))
                        .filter(session -> session.getStart().isBefore(LocalDateTime.now())
                                && session.getEnd().isAfter(LocalDateTime.now()))
                        .findFirst()
                ).isPresent();
    }


    @Override
    public void registerNewUserFromJwt(Jwt jwt) {
        String keycloakId = jwt.getSubject();
        userRepository.findByKeycloakId(keycloakId)
                .map(u -> {
                    List<String> roles = extractRoles(jwt);
                    u.setRole(mapToRoleEntity(roles));
                    return userRepository.save(u);
                })
                .orElseGet(() -> {
                    User u = new User();
                    u.setKeycloakId(keycloakId);
                    u.setUsername(jwt.getClaim("preferred_username"));
                    u.setEmail(jwt.getClaim("email"));
                    u.setFirstName(jwt.getClaim("given_name"));
                    u.setLastName(jwt.getClaim("family_name"));
                    u.setDateOfBirth(LocalDate.now());    // or parse birthdate claim
                    List<String> roles = extractRoles(jwt);
                    u.setRole(mapToRoleEntity(roles));
                    return userRepository.save(u);
                });
    }

    // Helper to pull out the raw role names
    private List<String> extractRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) return List.of();
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) realmAccess.get("roles");
        return roles == null ? List.of() : roles;
    }

    // Map the first matching string role to your Role entity; tweak as needed
    private Role mapToRoleEntity(List<String> roles) {
        if (roles.contains("ADMIN")) {
            return new Role(1L);   // ADMIN
        } else {
            return new Role(2L);   // USER (or however your IDs map)
        }
    }

}
