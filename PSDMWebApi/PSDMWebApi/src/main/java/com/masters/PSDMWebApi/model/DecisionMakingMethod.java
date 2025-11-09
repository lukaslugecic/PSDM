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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_decision_making_method")
    private Long id;

    @Column(name = "decision_making_method_title", nullable = false)
    private String title;

    @Column(name = "decision_making_method_description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "decisionMakingMethod")
    private List<Session> sessions;

    public DecisionMakingMethod (Long id) {
        this.id = id;
    }
}
