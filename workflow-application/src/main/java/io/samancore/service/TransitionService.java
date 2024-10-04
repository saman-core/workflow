package io.samancore.service;

import io.samancore.model.Transition;
import io.smallrye.mutiny.Uni;
import jakarta.json.JsonArray;

import java.util.List;

public interface TransitionService {

    Uni<Transition> getByProductAndTransition(Long productId, String transitionId);

    Uni<List<Transition>> getAllByProductAndStateFrom(Long productId, String stateFromId);

    Uni<List<Transition>> getAllByProductAndStateFromAndRoles(Long productId, String stateFromId, JsonArray userRolesJsonArray);

    Uni<List<Transition>> getAllByStateInitialAndProduct(Long productId);

    Uni<List<Transition>> getAllByStateInitialAndProductAndRoles(Long productId, JsonArray userRolesJsonArray);

    Uni<Boolean> validateCanBeApplied(Long productId, String stateId, String transitionId);
}
