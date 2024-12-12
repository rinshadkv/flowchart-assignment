package com.assignment.flowchart.Repository;

import com.assignment.flowchart.domain.Edge;
import com.assignment.flowchart.domain.Node;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EdgeRepository extends JpaRepository<Edge, Long> {
    boolean existsBySourceNodeAndTargetNode(Node source, Node target);


    void deleteAllByFlowchartId(Long chartId);

    List<Edge> findAllByFlowchartId(Long id);

    List<Edge> findAllBySourceNodeId(Long nodeId);
}
