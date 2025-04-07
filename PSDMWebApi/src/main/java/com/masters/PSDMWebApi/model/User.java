package com.masters.PSDMWebApi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private Long id;

    private String name;
    private String surname;
    private String email;

    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany(mappedBy = "users")
    private List<Session> sessions;

    @OneToMany(mappedBy = "user")
    private List<Solution> solutions;

    @OneToMany(mappedBy = "user")
    private List<Vote> votes;

    public User(Long id) {
        this.id = id;
    }
}
