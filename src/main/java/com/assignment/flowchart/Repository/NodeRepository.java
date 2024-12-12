package com.assignment.flowchart.Repository;

import com.assignment.flowchart.domain.Flowchart;
import com.assignment.flowchart.domain.Node;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NodeRepository extends JpaRepository<Node, Long> {


    List<Node> findByFlowchartId(Long chartId);

    void deleteAllByFlowchartId(Long id);
}
