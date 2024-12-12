package com.assignment.flowchart.controller;

import com.assignment.flowchart.domain.Edge;
import com.assignment.flowchart.domain.Flowchart;
import com.assignment.flowchart.domain.Node;
import com.assignment.flowchart.dto.FlowchartDTO;
import com.assignment.flowchart.service.FlowchartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flowcharts")
public class FlowchartController {

    @Autowired
    private FlowchartService service;

    @PostMapping
    public Flowchart createFlowchart(@Valid @RequestBody FlowchartDTO flowchart) {
        return service.createFlowchart(flowchart);
    }

    @GetMapping("/{id}")
    public Flowchart getFlowchartById(@PathVariable Long id) {
        return service.getFlowchartById(id);
    }

    @PutMapping("/{id}")
    public Flowchart updateFlowchart(@PathVariable Long id, @Valid @RequestBody FlowchartDTO flowchart) {
        return service.updateFlowchart(id, flowchart);
    }

    @GetMapping("")
    public List<FlowchartDTO> getAllFlowcharts() {
        return service.getAllFlowcharts();
    }

    @DeleteMapping("/{id}")
    public void deleteFlowchart(@PathVariable Long id) {
        service.deleteFlowchart(id);
    }

    @GetMapping("/{nodeId}/outgoing-edges")
    public List<Edge> getOutgoingEdges(@PathVariable Long nodeId) {
        return service.getOutgoingEdges(nodeId);
    }


    @GetMapping("/{id}/connected-nodes/{startNodeId}")
    public List<Node> getConnectedNodes(@PathVariable Long id, @PathVariable Long startNodeId) {

        return service.getConnectedNodes(startNodeId, id);
    }

}

