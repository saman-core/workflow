package io.samancore.workflow.app.util;

import io.samancore.workflow.model.State;
import io.samancore.workflow.model.Transition;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.samancore.workflow.app.util.GraphCache.GRAPH;

public class GraphQueries {

    private GraphQueries() {
    }

    public static State getVertex(String vertexId) {
        return GRAPH.vertexSet().stream()
                .filter(vertex -> vertex.id().equals(vertexId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Vertex not found"));
    }

    public static Transition getEdge(String edgeId) {
        return GRAPH.edgeSet().stream()
                .filter(edge -> edge.id().equals(edgeId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Edge not found"));
    }

    public static List<Transition> getEdgesFromVertex(String vertexId) {
        State state = new State(vertexId, null, null, null, null);
        return GRAPH.outgoingEdgesOf(state).stream().toList();
    }

    public static State getTargetVertex(String edgeId) {
        Transition transition = new Transition(edgeId, null, null, null, null, null);
        return GRAPH.getEdgeTarget(transition);
    }

    public static State getSourceVertex(String edgeId) {
        Transition transition = new Transition(edgeId, null, null, null, null, null);
        return GRAPH.getEdgeSource(transition);
    }

    public static Set<State> getVerticesNotTarget() {
        Set<State> targetVertices = GRAPH.edgeSet().stream()
                .map(GRAPH::getEdgeTarget)
                .collect(Collectors.toSet());
        return GRAPH.vertexSet().stream()
                .filter(vertex -> !targetVertices.contains(vertex))
                .collect(Collectors.toSet());
    }
}
