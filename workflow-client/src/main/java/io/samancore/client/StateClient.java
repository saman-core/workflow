package io.samancore.client;

import io.samancore.model.State;
import io.smallrye.mutiny.Uni;

public interface StateClient {
    Uni<State> getByProductAndStateId(Long productId, String stateId);

    Uni<State> getInitialByProductId(Long productId);
}