package io.samancore.workflow.app.util;

import io.samancore.workflow.model.State;
import io.samancore.workflow.model.Transition;
import org.jgrapht.graph.DefaultDirectedGraph;

public class GraphCache {
    public static final DefaultDirectedGraph<State, Transition> GRAPH = new DefaultDirectedGraph<>(Transition.class);

    private GraphCache() {
    }
}
