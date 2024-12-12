package com.assignment.flowchart.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class EdgeDTO {
    private Long id;
    @NonNull
    private Long sourceNode;
    @NonNull
    private Long targetNode;
    private String label;
}
