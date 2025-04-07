package com.masters.PSDMWebApi.model.id;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class VoteId implements Serializable {
    private Long user;
    private Long solution;

    // equals and hashCode (important for composite keys)
}
