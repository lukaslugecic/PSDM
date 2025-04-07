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
@Table(name = "decision_making_method")
public class DecisionMakingMethod {
    @Id
    private Long id;

    private String decisionMakingMethodTitle;
    private String decisionMakingMethodDescription;

    @OneToMany(mappedBy = "decisionMakingMethod")
    private List<Session> sessions;

    public DecisionMakingMethod (Long id) {
        this.id = id;
    }
}
