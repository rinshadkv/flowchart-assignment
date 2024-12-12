package com.assignment.flowchart.domain;

import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Table(name = "flowchart")
public class Flowchart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;


    public Flowchart() {

    }

    public Flowchart(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
