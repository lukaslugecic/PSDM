package com.masters.PSDMWebApi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "problem_solving_method")
public class ProblemSolvingMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_problem_solving_method")
    private Long id;

    @Column(name = "problem_solving_method_title", nullable = false)
    private String title;

    @Column(name = "problem_solving_method_description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "problemSolvingMethod")
    private List<Session> sessions;

    @OneToMany(mappedBy = "method", fetch = FetchType.EAGER)
    private List<ProblemSolvingMethodStep> methodSteps = new ArrayList<>();

    public ProblemSolvingMethod (Long id) {
        this.id = id;
    }
}
