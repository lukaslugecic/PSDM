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
@Table(name = "problem")
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_problem")
    private Long id;

    @Column(name = "problem_title", nullable = false)
    private String title;

    @Column(name = "problem_description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private User moderator;

    @Column(name = "problem_start", nullable = false)
    private LocalDateTime start;

    @Column(name = "problem_end")
    private LocalDateTime end;

    @OneToMany(mappedBy = "problem")
    private List<Solution> solutions;

    @OneToMany(mappedBy = "problem")
    private List<Session> sessions;

    public Problem (Long id) {
        this.id = id;
    }
}
