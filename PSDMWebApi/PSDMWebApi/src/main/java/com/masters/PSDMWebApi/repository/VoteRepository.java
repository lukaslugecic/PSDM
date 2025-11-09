package com.masters.PSDMWebApi.repository;

import com.masters.PSDMWebApi.model.Vote;
import com.masters.PSDMWebApi.model.id.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, VoteId> {
    List<Vote> findBySolutionId(Integer solutionId);
    List<Vote> findByUserId(Integer userId);
}

