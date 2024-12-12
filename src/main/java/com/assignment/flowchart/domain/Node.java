package com.assignment.flowchart.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class Node implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flowchart_id")
    private Flowchart flowchart;


    public Node() {
    }

    public Node(String label, Flowchart flowchart) {
        this.label = label;
        this.flowchart = flowchart;
    }
}
