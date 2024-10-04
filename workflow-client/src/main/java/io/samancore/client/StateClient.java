package io.samancore.client;

import io.samancore.model.State;

public interface StateClient {
    State getByProductAndStateId(Long productId, String stateId);

    State getInitialByProductId(Long productId);
}