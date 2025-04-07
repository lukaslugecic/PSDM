package com.masters.PSDMWebApi.repository;

import com.masters.PSDMWebApi.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    List<Solution> findByProblemId(Long problemId);
    List<Solution> findBySessionId(Long sessionId);
    List<Solution> findByUserId(Long userId);
    Optional<Solution> findByProblemIdAndChosenTrue(Long problemId);
}
