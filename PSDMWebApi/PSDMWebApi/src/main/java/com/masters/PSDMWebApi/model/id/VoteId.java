package com.masters.PSDMWebApi.model.id;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class VoteId implements Serializable {
    private Long user;
    private Long solution;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VoteId that)) return false;
        return Objects.equals(user, that.getUser()) &&
                Objects.equals(solution, that.getSolution());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, solution);
    }
}
