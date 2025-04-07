package com.masters.PSDMWebApi.service;

import com.masters.PSDMWebApi.model.SessionUsers;
import com.masters.PSDMWebApi.model.id.SessionUsersId;
import java.util.List;
import java.util.Optional;

public interface SessionUsersService {
    List<SessionUsers> getAllSessionUserss();

    Optional<SessionUsers> getSessionUsersById(SessionUsersId id);

    SessionUsers saveSessionUsers(SessionUsers vote);

    void deleteSessionUsers(SessionUsersId id);
}
