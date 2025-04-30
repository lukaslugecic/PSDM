package com.masters.PSDMWebApi.repository;

import com.masters.PSDMWebApi.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    List<Solution> findBySessionId(Long sessionId);
}
