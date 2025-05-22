package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.model.User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByKeycloakId(String keycloakId);

    Long getCurrentSessionId(Long userId, boolean isSubSession);

    Boolean checkParentSession(Long userId);

    void registerNewUserFromJwt(Jwt jwt);
}
