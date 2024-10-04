package io.samancore.client.rest;

import io.samancore.client.StateClient;
import io.samancore.client.rest.microprofile.StateRestClient;
import io.samancore.model.State;
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
    public State getByProductAndStateId(Long productId, String stateId) {
        log.debugf("StateRestClientWrapper.getByProductAndStateId productId: %d stateId: %s", productId, stateId);
        return service.getByProductAndStateId(productId, stateId);
    }

    @Override
    public State getInitialByProductId(Long productId) {
        log.debugf("StateRestClientWrapper.getInitialByProductId productid %d", productId);
        return service.getInitialByProductId(productId);
    }
}