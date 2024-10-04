package io.samancore.util;

import io.samancore.cache.WorkflowCache;
import io.samancore.model.State;
import io.samancore.model.Transition;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class GraphUtil {

    @Inject
    Logger log;

    @Inject
    WorkflowCache workflowCache;

    public Uni<State> getByProductAndStateId(Long productId, String stateId) {
        log.debugf("GraphUtil.getByProductAndStateId productId %d stateId: %s", productId, stateId);
        var workflow = workflowCache.getWorkflowGraph(productId);
        return Uni.createFrom().item(workflow.vertexSet().stream().filter(state -> state.getId().equals(stateId)).findFirst()
                .orElseThrow(NotFoundException::new));
    }

    public Uni<State> getInitialByProduct(Long productId) {
        log.debugf("GraphUtil.getInitialByProduct productId %d statusFromId: %s", productId);
        var workflow = workflowCache.getWorkflowGraph(productId);
        return Uni.createFrom().item(workflow.vertexSet().stream().filter(State::getIsInitial).findFirst()
                .orElseThrow(NotFoundException::new));
    }

    public Uni<Transition> getTransitionByProductAndTransition(Long productId, String transitionId) {
        log.debugf("GraphUtil.getTransitionByProductAndTransition productId %d transitionId: %s", productId, transitionId);
        var workflow = workflowCache.getWorkflowGraph(productId);
        return Uni.createFrom().item(workflow.edgeSet().stream().filter(transition -> transition.getId().equals(transitionId)).findFirst()
                .orElseThrow(NotFoundException::new));
    }

    public Uni<List<Transition>> getAllTransitionByProductAndStateFrom(Long productId, String stateFromId) {
        log.debugf("GraphUtil.getAllTransitionByProductAndStateFrom productId %d statusFromId: %s", productId, stateFromId);
        var workflow = workflowCache.getWorkflowGraph(productId);
        var stateFrom = workflow.vertexSet().stream().filter(state -> state.getId().equals(stateFromId)).findFirst()
                .orElseThrow(NotFoundException::new);
        return Uni.createFrom().item(workflow.outgoingEdgesOf(stateFrom).stream().toList());
    }

    public Uni<List<Transition>> getAllTransitionByStateInitialAndProduct(Long productId) {
        log.debugf("GraphUtil.getAllTransitionByStateInitialAndProduct productId: %d", productId);
        var workflow = workflowCache.getWorkflowGraph(productId);
        var stateInitial = workflow.vertexSet().stream().filter(State::getIsInitial).findFirst()
                .orElseThrow(NotFoundException::new);
        return Uni.createFrom().item(workflow.outgoingEdgesOf(stateInitial).stream().toList());
    }

    public Uni<Boolean> validateIfTransitionCanBeApplied(Long productId, String stateFromId, String transitionId) {
        log.debugf("GraphUtil.validateIfTransitionCanBeApplied productId %d statusFromId: %s transitionId: %s", productId, stateFromId, transitionId);
        var workflow = workflowCache.getWorkflowGraph(productId);
        var state = workflow.vertexSet().stream().filter(state1 -> state1.getId().equals(stateFromId)).findFirst()
                .orElseThrow(NotFoundException::new);
        return Uni.createFrom().item(workflow.outgoingEdgesOf(state).stream().anyMatch(transition -> transition.getId().equals(transitionId)));
    }
}
