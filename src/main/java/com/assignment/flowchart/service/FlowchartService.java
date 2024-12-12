package com.assignment.flowchart.service;

import com.assignment.flowchart.Repository.EdgeRepository;
import com.assignment.flowchart.Repository.FlowchartRepository;
import com.assignment.flowchart.Repository.NodeRepository;
import com.assignment.flowchart.domain.Edge;
import com.assignment.flowchart.domain.Flowchart;
import com.assignment.flowchart.domain.Node;
import com.assignment.flowchart.dto.EdgeDTO;
import com.assignment.flowchart.dto.FlowchartDTO;
import com.assignment.flowchart.dto.NodeDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlowchartService {

    @Autowired
    private FlowchartRepository flowchartRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private NodeRepository nodeRepository;

    // --- Create Flowchart ---
    public Flowchart createFlowchart(FlowchartDTO flowchartDTO) {
        Flowchart flowchart = flowchartRepository.save(new Flowchart(flowchartDTO.getName()));
        createOrUpdateNodes(flowchartDTO, flowchart);

        if (!validateFlowchart(flowchart.getId())) {
            throw new IllegalArgumentException("The flowchart structure is invalid. Rolling back transaction.");
        }

        return flowchart;
    }

    // --- Get Flowchart by ID ---
    public FlowchartDTO getFlowchartById(Long id) {
        Flowchart flowchart = flowchartRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Flowchart not found"));

        FlowchartDTO dto = new FlowchartDTO();
        dto.setId(flowchart.getId());
        dto.setName(flowchart.getName());

        List<Node> nodes = nodeRepository.findByFlowchartId(flowchart.getId());
        List<Edge> edges = edgeRepository.findAllByFlowchartId(flowchart.getId());

        // Map nodes and edges to DTOs
        dto.setNodes(mapNodesToDTO(nodes));
        dto.setEdges(mapEdgesToDTO(edges));

        return dto;
    }

    // --- Get All Flowcharts ---
    public List<FlowchartDTO> getAllFlowcharts() {
        List<Flowchart> flowcharts = flowchartRepository.findAll();
        return flowcharts.stream()
                .map(chart -> getFlowchartById(chart.getId()))
                .collect(Collectors.toList());
    }

    // --- Delete Flowchart ---
    public void deleteFlowchart(Long id) {
        flowchartRepository.deleteById(id);
        nodeRepository.deleteAllByFlowchartId(id);
        edgeRepository.deleteAllByFlowchartId(id);
    }

    // --- Get Outgoing Edges from a Node ---
    public List<Edge> getOutgoingEdges(Long nodeId) {
        return edgeRepository.findAllBySourceNodeId(nodeId);
    }

    // --- Update Flowchart ---
    public Flowchart updateFlowchart(Long flowchartId, FlowchartDTO flowchartDTO) {
        Flowchart flowchart = flowchartRepository.findById(flowchartId)
                .orElseThrow(() -> new EntityNotFoundException("Flowchart not found with ID: " + flowchartId));

        createOrUpdateNodes(flowchartDTO, flowchart);

        if (!validateFlowchart(flowchart.getId())) {
            throw new IllegalArgumentException("The flowchart structure is invalid. Rolling back transaction.");
        }

        return flowchart;
    }

    // --- Get Connected Nodes ---
    public List<Node> getConnectedNodes(Long startNodeId, Long flowchartId) {
        Map<Long, List<Long>> adjacencyList = createAdjacencyList(flowchartId);

        Set<Long> visited = new HashSet<>();
        List<Node> result = new ArrayList<>();
        Queue<Long> queue = new LinkedList<>();

        queue.add(startNodeId);
        visited.add(startNodeId);

        while (!queue.isEmpty()) {
            Long currentNodeId = queue.poll();
            Node currentNode = nodeRepository.findById(currentNodeId)
                    .orElseThrow(() -> new EntityNotFoundException("Node with ID " + currentNodeId + " not found"));

            result.add(currentNode);
            for (Long neighborId : adjacencyList.getOrDefault(currentNodeId, Collections.emptyList())) {
                if (!visited.contains(neighborId)) {
                    visited.add(neighborId);
                    queue.add(neighborId);
                }
            }
        }

        return result;
    }

    // --- Validate Flowchart ---
    private boolean validateFlowchart(Long id) {
        return isGraphValid(id) && !hasDuplicateEdges(id) && !hasSelfLoops(id) && !hasIsolatedNodes(id);
    }

    // --- Check Graph Validity ---
    private boolean isGraphValid(Long id) {
        Map<Long, List<Long>> adjacencyList = createAdjacencyList(id);

        Set<Long> visited = new HashSet<>();
        Set<Long> recursionStack = new HashSet<>();

        for (Long node : adjacencyList.keySet()) {
            if (detectCycle(node, adjacencyList, visited, recursionStack)) {
                return false;
            }
        }

        return true;
    }

    // --- Check for Duplicate Edges ---
    private boolean hasDuplicateEdges(Long flowchartId) {
        Set<String> edgePairs = new HashSet<>();
        for (Edge edge : edgeRepository.findAllByFlowchartId(flowchartId)) {
            String pair = edge.getSourceNode().getId() + "-" + edge.getTargetNode().getId();
            if (!edgePairs.add(pair)) {
                return true;
            }
        }
        return false;
    }

    // --- Check for Self Loops ---
    private boolean hasSelfLoops(Long flowchartId) {
        return edgeRepository.findAllByFlowchartId(flowchartId).stream()
                .anyMatch(edge -> edge.getSourceNode().getId().equals(edge.getTargetNode().getId()));
    }

    // --- Check for Isolated Nodes ---
    private boolean hasIsolatedNodes(Long flowchartId) {
        Set<Long> connectedNodes = new HashSet<>();
        edgeRepository.findAllByFlowchartId(flowchartId).forEach(edge -> {
            connectedNodes.add(edge.getSourceNode().getId());
            connectedNodes.add(edge.getTargetNode().getId());
        });

        List<Node> allNodes = nodeRepository.findByFlowchartId(flowchartId);
        return allNodes.stream().anyMatch(node -> !connectedNodes.contains(node.getId()));
    }

    // --- Detect Cycle in Graph ---
    private boolean detectCycle(Long node, Map<Long, List<Long>> adjacencyList, Set<Long> visited, Set<Long> recursionStack) {
        if (recursionStack.contains(node)) return true;
        if (visited.contains(node)) return false;

        visited.add(node);
        recursionStack.add(node);

        for (Long neighbor : adjacencyList.getOrDefault(node, Collections.emptyList())) {
            if (detectCycle(neighbor, adjacencyList, visited, recursionStack)) {
                return true;
            }
        }

        recursionStack.remove(node);
        return false;
    }

    // --- Helper Methods for Node and Edge Management ---
    private Map<Long, List<Long>> createAdjacencyList(Long flowchartId) {
        Map<Long, List<Long>> adjacencyList = new HashMap<>();
        for (Edge edge : edgeRepository.findAllByFlowchartId(flowchartId)) {
            adjacencyList.computeIfAbsent(edge.getSourceNode().getId(), k -> new ArrayList<>())
                    .add(edge.getTargetNode().getId());
        }
        return adjacencyList;
    }

    private List<NodeDTO> mapNodesToDTO(List<Node> nodes) {
        return nodes.stream()
                .map(node -> {
                    NodeDTO nodeDTO = new NodeDTO();
                    nodeDTO.setId(node.getId());
                    nodeDTO.setLabel(node.getLabel());
                    return nodeDTO;
                })
                .collect(Collectors.toList());
    }

    private List<EdgeDTO> mapEdgesToDTO(List<Edge> edges) {
        return edges.stream()
                .map(edge -> {
                    EdgeDTO edgeDTO = new EdgeDTO();
                    edgeDTO.setId(edge.getId());
                    edgeDTO.setSourceNode(edge.getSourceNode().getId());
                    edgeDTO.setTargetNode(edge.getTargetNode().getId());
                    edgeDTO.setLabel(edge.getSourceNode().getLabel());
                    return edgeDTO;
                })
                .collect(Collectors.toList());
    }

    private void createOrUpdateNodes(FlowchartDTO flowchartDTO, Flowchart flowchart) {
        Map<Long, Node> nodeMapping = new HashMap<>();
        List<Node> existingNodes = nodeRepository.findByFlowchartId(flowchart.getId());

        flowchartDTO.getNodes().forEach(dto -> {
            if (nodeMapping.containsKey(dto.getNodeIdentifier())) {
                throw new DuplicateKeyException("Node ID " + dto.getNodeIdentifier() + " must be unique.");
            }

            Node node = dto.getId() != null ? existingNodes.stream()
                    .filter(existing -> Objects.equals(existing.getId(), dto.getId()))
                    .findFirst().orElseThrow(() -> new EntityNotFoundException("Node not found with ID: " + dto.getId()))
                    : new Node();
            node.setFlowchart(flowchart);
            node.setLabel(dto.getLabel());
            nodeMapping.put(dto.getNodeIdentifier(), node);

            nodeRepository.save(node);
        });

        // Delete nodes that are no longer part of the updated flowchart
        List<Node> nodesToDelete = existingNodes.stream()
                .filter(existing -> nodeMapping.entrySet().stream().noneMatch(dto -> Objects.equals(existing, dto.getValue())))
                .toList();
        nodeRepository.deleteAll(nodesToDelete);

        // Update edges after node creation/updating
        updateEdges(flowchartDTO.getEdges(), nodeMapping, flowchart);
    }

    private void updateEdges(List<EdgeDTO> edges, Map<Long, Node> nodeMapping, Flowchart flowchart) {
        edgeRepository.deleteAllByFlowchartId(flowchart.getId());
        List<Edge> edgesToAdd = new ArrayList<>();

        edges.forEach(dto -> {
            Node source = nodeMapping.get(dto.getSourceNode());
            Node target = nodeMapping.get(dto.getTargetNode());

            if (source == null || target == null) {
                throw new IllegalArgumentException("Invalid source or target node ID for edge.");
            } else if (source.equals(target)) {
                throw new IllegalArgumentException("Source and target cannot be the same.");
            }

            Edge edge = new Edge();
            edge.setFlowchart(flowchart);
            edge.setSourceNode(source);
            edge.setTargetNode(target);
            edgesToAdd.add(edge);
        });

        edgeRepository.saveAll(edgesToAdd);
    }
}
