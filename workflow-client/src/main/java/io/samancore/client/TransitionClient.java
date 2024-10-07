package io.samancore.client;

import io.samancore.model.Transition;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface TransitionClient {

    Uni<Transition> getByProductAndTransition(Long productId, String transitionId);

    Uni<List<Transition>> getAllByProductAndStateFrom(Long productId, String stateFromId);

    Uni<List<Transition>> getAllByProductAndStateFromAndRoles(Long productId, String stateFromId);

    Uni<List<Transition>> getAllByStateInitialAndProduct(Long productId);

    Uni<List<Transition>> getAllByStateInitialAndProductAndRoles(Long productId);

    Uni<Boolean> validateCanBeApplied(Long productId, String stateId, String transitionId);
}