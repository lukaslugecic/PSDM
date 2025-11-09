package com.masters.PSDMWebApi.model.id;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class ProblemSolvingMethodStepId implements Serializable {
    private Long method;
    private Long step;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProblemSolvingMethodStepId that)) return false;
        return Objects.equals(method, that.getMethod()) &&
                Objects.equals(step, that.getStep());
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, step);
    }
}

