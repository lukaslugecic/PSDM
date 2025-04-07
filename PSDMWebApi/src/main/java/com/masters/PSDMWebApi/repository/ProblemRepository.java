package com.masters.PSDMWebApi.repository;

import com.masters.PSDMWebApi.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
}

