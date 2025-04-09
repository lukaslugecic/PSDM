package com.masters.PSDMWebApi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "attribute")
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_attribute")
    private Long id;

    @Column(name = "attribute_title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private Solution solution;
}

