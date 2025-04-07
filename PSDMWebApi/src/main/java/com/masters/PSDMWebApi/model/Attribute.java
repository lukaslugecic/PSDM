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
    private Long id;

    private String attributeTitle;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private Solution solution;
}

