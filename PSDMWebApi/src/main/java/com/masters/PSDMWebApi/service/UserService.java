package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Long getCurrentSessionId(Long userId, boolean isSubSession);
}
