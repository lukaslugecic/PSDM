package com.masters.PSDMWebApi.model.id;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ProblemSolvingMethodStepId implements Serializable {
    private Long method;
    private Long step;

    // equals and hashCode
}

