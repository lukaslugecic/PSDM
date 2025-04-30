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
@Table(name = "solution")
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solution")
    private Long id;

    @Column(name = "solution_title", nullable = false)
    private String title;

    @Column(name = "solution_description", nullable = false)
    private String description;

    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    @Column(name = "chosen")
    private Boolean chosen;

    @Column(name = "grouped")
    private Boolean grouped;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @OneToMany(mappedBy = "solution")
    private List<Attribute> attributes;

    @OneToMany(mappedBy = "solution")
    private List<Vote> votes;

    public Solution(Long solutionId) {
        this.id = solutionId;
    }
}
