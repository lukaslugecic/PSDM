package com.masters.PSDMWebApi.service.impl;


import com.masters.PSDMWebApi.model.Session;
import com.masters.PSDMWebApi.model.User;
import com.masters.PSDMWebApi.repository.UserRepository;
import com.masters.PSDMWebApi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
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
                .orElseThrow(() -> new NoSuchElementException("Active session not found for user ID: " + userId + " " + isSubSession));

        log.warn("Found active session for user ID: {} {} and it is {}", userId, isSubSession, id);

        return id;
    }

}
