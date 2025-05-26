package io.samancore.workflow.app.util;

import io.samancore.workflow.model.State;
import io.samancore.workflow.model.Transition;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.samancore.workflow.app.util.GraphCache.GRAPHS;

public class GraphQueries {

    private GraphQueries() {
    }

    public static State getVertex(String productName, String vertexId) {
        return GRAPHS.get(productName).vertexSet().stream()
                .filter(vertex -> vertex.id().equals(vertexId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Vertex not found"));
    }

    public static Transition getEdge(String productName, String edgeId) {
        return GRAPHS.get(productName).edgeSet().stream()
                .filter(edge -> edge.id().equals(edgeId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Edge not found"));
    }

    public static List<Transition> getEdgesFromVertex(String productName, String vertexId) {
        State state = new State(vertexId, null, null, null, null);
        return GRAPHS.get(productName).outgoingEdgesOf(state).stream().toList();
    }

    public static State getTargetVertex(String productName, String edgeId) {
        Transition transition = new Transition(edgeId, null, null, null, null, null);
        return GRAPHS.get(productName).getEdgeTarget(transition);
    }

    public static State getSourceVertex(String productName, String edgeId) {
        Transition transition = new Transition(edgeId, null, null, null, null, null);
        return GRAPHS.get(productName).getEdgeSource(transition);
    }

    public static Set<State> getVerticesNotTarget(String productName) {
        Set<State> targetVertices = GRAPHS.get(productName).edgeSet().stream()
                .map(GRAPHS.get(productName)::getEdgeTarget)
                .collect(Collectors.toSet());
        return GRAPHS.get(productName).vertexSet().stream()
                .filter(vertex -> !targetVertices.contains(vertex))
                .collect(Collectors.toSet());
    }
}
