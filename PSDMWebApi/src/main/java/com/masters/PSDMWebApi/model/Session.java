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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_session")
    private Long id;

    @Column(name = "session_start", nullable = false)
    private LocalDateTime start;

    @Column(name = "session_end")
    private LocalDateTime end;

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

    @ManyToOne
    @JoinColumn(name = "parent_session_id")
    private Session parentSession;

    @OneToMany(mappedBy = "parentSession")
    private List<Session> subSessions;

    public Session (Long id) {
        this.id = id;
    }
}
