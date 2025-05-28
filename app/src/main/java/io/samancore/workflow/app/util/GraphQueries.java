package io.samancore.workflow.app.util;

import io.samancore.workflow.model.State;
import io.samancore.workflow.model.Transition;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

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

    public static List<Transition> getInitialEdges(String productName) {
        return GRAPHS.get(productName).vertexSet().stream()
                .filter(State::isInitial)
                .findFirst()
                .map(state -> GRAPHS.get(productName).outgoingEdgesOf(state).stream().toList())
                .orElseGet(List::of);

    }
}
