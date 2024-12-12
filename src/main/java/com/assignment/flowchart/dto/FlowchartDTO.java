package com.assignment.flowchart.dto;

import com.assignment.flowchart.domain.Flowchart;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FlowchartDTO extends Flowchart {

    private List<NodeDTO> nodes;
    private List<EdgeDTO> edges;


}
