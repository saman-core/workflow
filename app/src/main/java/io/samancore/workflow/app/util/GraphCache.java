package io.samancore.workflow.app.util;

import io.samancore.workflow.model.State;
import io.samancore.workflow.model.Transition;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.HashMap;
import java.util.Map;

public class GraphCache {
    public static final Map<String, DefaultDirectedGraph<State, Transition>> GRAPHS = new HashMap<>();

    private GraphCache() {
    }

    public static DefaultDirectedGraph<State, Transition> emptyGraph() {
        return new DefaultDirectedGraph<>(Transition.class);
    }
}
