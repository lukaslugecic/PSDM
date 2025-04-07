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
    private Long id;

    private String solutionTitle;
    private String solutionDescription;

    private LocalDateTime createdTime;
    private Boolean chosen;

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
