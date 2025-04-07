package com.masters.PSDMWebApi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "step")
public class Step {
    @Id
    private Long id;

    private String stepTitle;
    private String stepDescription;

    @OneToMany(mappedBy = "step")
    private List<ProblemSolvingMethodStep> methodSteps;

    public Step (Long id) {
        this.id = id;
    }
}
