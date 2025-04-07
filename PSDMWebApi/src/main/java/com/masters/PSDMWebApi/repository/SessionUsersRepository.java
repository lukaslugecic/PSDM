package com.masters.PSDMWebApi.repository;

import com.masters.PSDMWebApi.model.SessionUsers;
import com.masters.PSDMWebApi.model.id.SessionUsersId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SessionUsersRepository extends JpaRepository<SessionUsers, SessionUsersId> {
    List<SessionUsers> findBySessionId(Integer sessionId);
    List<SessionUsers> findByUserId(Integer userId);
}

