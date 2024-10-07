package io.samancore.client.rest;

import io.samancore.client.StateClient;
import io.samancore.client.rest.microprofile.StateRestClient;
import io.samancore.model.State;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
public class StateRestClientWrapper implements StateClient {

    @Inject
    Logger log;

    @Inject
    @RestClient
    StateRestClient service;

    @Override
    public Uni<State> getByProductAndStateId(Long productId, String stateId) {
        log.debugf("StateRestClientWrapper.getByProductAndStateId productId: %d stateId: %s", productId, stateId);
        return service.getByProductAndStateId(productId, stateId);
    }

    @Override
    public Uni<State> getInitialByProductId(Long productId) {
        log.debugf("StateRestClientWrapper.getInitialByProductId productid %d", productId);
        return service.getInitialByProductId(productId);
    }
}