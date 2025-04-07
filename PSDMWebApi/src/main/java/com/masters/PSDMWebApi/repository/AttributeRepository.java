package com.masters.PSDMWebApi.repository;

import com.masters.PSDMWebApi.model.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    List<Attribute> findBySolutionId(Long solutionId);
}
