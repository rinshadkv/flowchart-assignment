package com.assignment.flowchart.dto;

import com.assignment.flowchart.domain.Node;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(value = { "flowchart" })
public class NodeDTO extends Node {
    private Long id;
    @NonNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long nodeIdentifier;
    private String label;

}
