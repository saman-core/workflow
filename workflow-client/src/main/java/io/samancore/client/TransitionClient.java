package io.samancore.client;

import io.samancore.model.Transition;
import jakarta.json.JsonArray;

import java.util.List;

public interface TransitionClient {

    Transition getByProductAndTransition(Long productId, String transitionId);

    List<Transition> getAllByProductAndStateFrom(Long productId, String stateFromId);

    List<Transition> getAllByProductAndStateFromAndRoles(Long productId, String stateFromId, JsonArray userRolesJsonArray);

    List<Transition> getAllByStateInitialAndProduct(Long productId);

    List<Transition> getAllByStateInitialAndProductAndRoles(Long productId, JsonArray userRolesJsonArray);

    Boolean validateCanBeApplied(Long productId, String stateId, String transitionId);
}