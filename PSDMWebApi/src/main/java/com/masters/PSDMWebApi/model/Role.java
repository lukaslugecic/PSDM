package com.masters.PSDMWebApi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    private Long id;

    private String roleTitle;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    public Role (Long id) {
        this.id = id;
    }
}
