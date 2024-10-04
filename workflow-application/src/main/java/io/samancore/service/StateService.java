package io.samancore.service;

import io.samancore.model.State;
import io.smallrye.mutiny.Uni;

public interface StateService {

    Uni<State> getByProductAndStateId(Long productId, String stateId);

    Uni<State> getInitialByProductId(Long productId);
}
