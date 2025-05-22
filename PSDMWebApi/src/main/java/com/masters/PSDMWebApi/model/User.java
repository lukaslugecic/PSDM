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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    // this is the UUID Keycloak gives you
    @Column(name = "keycloak_id", nullable = false, unique = true, length = 36)
    private String keycloakId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "date_of_birth", nullable = false)
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
