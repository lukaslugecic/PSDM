package com.masters.PSDMWebApi.repository;

import com.masters.PSDMWebApi.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByProblemId(Long problemId);
}
