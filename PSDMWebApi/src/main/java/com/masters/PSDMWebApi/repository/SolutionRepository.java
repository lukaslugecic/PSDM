package com.masters.PSDMWebApi.repository;

import com.masters.PSDMWebApi.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    List<Solution> findBySessionId(Long sessionId);

    @Query("SELECT s FROM Solution s WHERE s.session.id = :sessionId OR s.session.parentSession.id = :sessionId")
    List<Solution> findBySessionIdOrParentSessionId(@Param("sessionId") Long sessionId);
}
