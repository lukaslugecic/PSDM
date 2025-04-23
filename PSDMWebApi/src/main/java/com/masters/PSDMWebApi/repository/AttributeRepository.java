package com.masters.PSDMWebApi.repository;

import com.masters.PSDMWebApi.model.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    List<Attribute> findBySolutionId(Long solutionId);

    @Query("SELECT value " +
            "FROM Attribute " +
            "WHERE title = 'Weight' " +
            "AND solution.id = :solutionId")
    Optional<String> findWeightAttributeBySolutionId(@Param("solutionId") Long solutionId);


    @Query("SELECT a.value " +
            "FROM Solution s " +
            "JOIN s.attributes a " +
            "WHERE a.title = 'Weight' " +
            "AND s.session.id = :sessionId")
    List<String> findWeightAttributesBySessionId(@Param("sessionId") Long sessionId);
}
