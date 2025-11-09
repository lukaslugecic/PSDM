package com.masters.PSDMWebApi.model;

import com.masters.PSDMWebApi.model.id.VoteId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "vote")
@IdClass(VoteId.class)
public class Vote {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "solution_id")
    private Solution solution;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "voting_time", nullable = false)
    private LocalDateTime votingTime;
}
