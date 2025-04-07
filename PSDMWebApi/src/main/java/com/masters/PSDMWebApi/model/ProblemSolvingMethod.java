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
    private Long id;

    private String problemSolvingMethodTitle;
    private String problemSolvingMethodDescription;

    @OneToMany(mappedBy = "problemSolvingMethod")
    private List<Session> sessions;

    @OneToMany(mappedBy = "method")
    private List<ProblemSolvingMethodStep> methodSteps;

    public ProblemSolvingMethod (Long id) {
        this.id = id;
    }
}
