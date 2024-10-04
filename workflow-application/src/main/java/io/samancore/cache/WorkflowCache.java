package io.samancore.cache;

import io.samancore.model.State;
import io.samancore.model.Transition;
import io.samancore.repository.TransitionRepository;
import io.samancore.transformer.TransitionTransformer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class WorkflowCache {

    @Inject
    Logger log;

    @Inject
    TransitionRepository transitionRepository;

    @Inject
    TransitionTransformer transitionTransformer;

    private Map<Long, DefaultDirectedGraph<State, Transition>> workflowMapByProduct;


    public Boolean init() {
        log.debug("WorkflowCache.init");
        workflowMapByProduct = new HashMap<>();
        var transitions = transitionRepository.getAll();
        var transitionByProductIdMap = transitions.stream().collect(Collectors.groupingBy(transitionEntity -> transitionEntity.getStateFrom().getWorkflow().getProductId()));
        transitionByProductIdMap.forEach((key, transitionEntities) -> {
            DefaultDirectedGraph<State, Transition> graph = new DefaultDirectedGraph<>(Transition.class);
            transitionEntities.forEach(transitionEntity -> {
                var transition = transitionTransformer.toModel(transitionEntity);
                var statusFrom = transition.getStateFrom();
                var statusTo = transition.getStateTo();
                graph.addVertex(statusFrom);
                graph.addVertex(statusTo);
                graph.addEdge(statusFrom, statusTo, transition);
            });
            workflowMapByProduct.put(key, graph);
        });
        return Boolean.TRUE;
    }

    public DefaultDirectedGraph<State, Transition> getWorkflowGraph(Long productId) {
        return workflowMapByProduct.get(productId);
    }
}
