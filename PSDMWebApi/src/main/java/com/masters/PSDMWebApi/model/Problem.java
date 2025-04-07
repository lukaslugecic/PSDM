package com.masters.PSDMWebApi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "problem")
public class Problem {
    @Id
    private Long id;

    private String problemTitle;

    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private User moderator;

    private LocalDateTime problemStart;
    private LocalDateTime problemEnd;

    @OneToMany(mappedBy = "problem")
    private List<Solution> solutions;

    @OneToMany(mappedBy = "problem")
    private List<Session> sessions;

    public Problem (Long id) {
        this.id = id;
    }
}
