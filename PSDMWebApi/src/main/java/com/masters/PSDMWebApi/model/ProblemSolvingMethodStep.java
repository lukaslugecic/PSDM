package com.masters.PSDMWebApi.model;


import com.masters.PSDMWebApi.model.id.ProblemSolvingMethodStepId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Entity
@Getter
@Setter
@Table(name = "problem_solving_method_step")
@IdClass(ProblemSolvingMethodStepId.class)
public class ProblemSolvingMethodStep {
    @Id
    @ManyToOne
    @JoinColumn(name = "method_id")
    private ProblemSolvingMethod method;

    @Id
    @ManyToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;

    @Column(name = "duration", nullable = false)
    private Duration duration;
}

