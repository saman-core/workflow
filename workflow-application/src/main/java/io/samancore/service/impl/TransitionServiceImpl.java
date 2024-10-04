package io.samancore.service.impl;

import io.samancore.model.Transition;
import io.samancore.model.TransitionRole;
import io.samancore.service.TransitionService;
import io.samancore.util.GraphUtil;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonValue;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class TransitionServiceImpl implements TransitionService {

    @Inject
    Logger log;

    @Inject
    GraphUtil graphUtil;

    @Override
    public Uni<Transition> getByProductAndTransition(Long productId, String transitionId) {
        return graphUtil.getTransitionByProductAndTransition(productId, transitionId);
    }

    @Override
    public Uni<List<Transition>> getAllByProductAndStateFrom(Long productId, String stateFromId) {
        log.debugf("TransitionServiceImpl.getAllByStateFrom productId %d statusFromId: %s", productId, stateFromId);
        return graphUtil.getAllTransitionByProductAndStateFrom(productId, stateFromId);
    }

    @Override
    public Uni<List<Transition>> getAllByProductAndStateFromAndRoles(Long productId, String stateFromId, JsonArray userRolesJsonArray) {
        log.debugf("TransitionServiceImpl.getAllByProductAndStateFromAndRoles productId %d stateFromId: %s", productId, stateFromId);
        var userRolesNameList = userRolesJsonArray.stream().map(JsonValue::toString)
                .map(value -> value.replace("\"", ""))
                .toList();
        return graphUtil.getAllTransitionByProductAndStateFrom(productId, stateFromId)
                .onItem().transform(transitions -> transitions.stream()
                        .filter(transition -> transition.getTransitionRoles().stream().map(TransitionRole::getRole)
                                .anyMatch(userRolesNameList::contains)).toList());
    }

    @Override
    public Uni<List<Transition>> getAllByStateInitialAndProduct(Long productId) {
        log.debugf("TransitionServiceImpl.getAllInitialByProduct statusFromId: %d", productId);
        return graphUtil.getAllTransitionByStateInitialAndProduct(productId);
    }

    @Override
    public Uni<List<Transition>> getAllByStateInitialAndProductAndRoles(Long productId, JsonArray userRolesJsonArray) {
        log.debugf("TransitionServiceImpl.getAllInitialByProduct statusFromId: %d", productId);
        var userRolesNameList = userRolesJsonArray.stream().map(JsonValue::toString)
                .map(value -> value.replace("\"", ""))
                .toList();
        return graphUtil.getAllTransitionByStateInitialAndProduct(productId)
                .onItem()
                .transform(entityList -> entityList.stream()
                        .filter(transition -> transition.getTransitionRoles().stream().map(TransitionRole::getRole)
                                .anyMatch(userRolesNameList::contains)).toList());
    }

    @Override
    public Uni<Boolean> validateCanBeApplied(Long productId, String stateId, String transitionId) {
        log.debugf("TransitionServiceImpl.validateCanBeApplied productId %d stateId: %s transitionId: %s", productId, stateId, transitionId);
        return graphUtil.validateIfTransitionCanBeApplied(productId, stateId, transitionId);
    }
}
