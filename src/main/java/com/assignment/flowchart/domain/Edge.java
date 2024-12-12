package com.assignment.flowchart.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class Edge implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "target_node_id")
    @NotNull
    @ToString.Exclude
    private Node targetNode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "source_node_id")
    @NotNull
    @ToString.Exclude
    private Node sourceNode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "flowchart_id")
    @NotNull
    @ToString.Exclude
    private Flowchart flowchart;

    public Edge() {
    }

    public Edge(Flowchart flowchart, Node sourceNode, Node targetNode) {
        this.flowchart = flowchart;
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
    }


}
