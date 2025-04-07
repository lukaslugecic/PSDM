package com.masters.PSDMWebApi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "session")
public class Session {
    @Id
    private Long id;

    private LocalDateTime sessionStart;
    private LocalDateTime sessionEnd;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "problem_solving_method_id")
    private ProblemSolvingMethod problemSolvingMethod;

    @ManyToOne
    @JoinColumn(name = "decision_making_method_id")
    private DecisionMakingMethod decisionMakingMethod;

    @ManyToMany
    @JoinTable(name = "session_users",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @OneToMany(mappedBy = "session")
    private List<Solution> solutions;

    public Session (Long id) {
        this.id = id;
    }
}
