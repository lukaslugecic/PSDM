package com.masters.PSDMWebApi.repository;

import com.masters.PSDMWebApi.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByModeratorId(long userId);
}

