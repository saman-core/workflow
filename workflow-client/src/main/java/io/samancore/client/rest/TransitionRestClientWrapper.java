package io.samancore.client.rest;

import io.samancore.client.TransitionClient;
import io.samancore.client.rest.microprofile.TransitionRestClient;
import io.samancore.model.Transition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class TransitionRestClientWrapper implements TransitionClient {

    @Inject
    Logger log;

    @Inject
    @RestClient
    TransitionRestClient service;

    @Override
    public Transition getByProductAndTransition(Long productId, String transitionId) {
        log.debugf("TransitionRestClientWrapper.validateCanBeApplied productId: %d stateId: %s transitionId: %s", transitionId);
        return service.getByProductAndTransition(productId, transitionId);
    }

    @Override
    public List<Transition> getAllByProductAndStateFrom(Long productId, String stateFromId) {
        log.debugf("TransitionRestClientWrapper.getAllByProductAndStateFrom productId: %d stateFromId: %s", productId, stateFromId);
        return service.getAllByProductAndStateFrom(productId, stateFromId);
    }

    @Override
    public List<Transition> getAllByProductAndStateFromAndRoles(Long productId, String stateFromId, JsonArray userRolesJsonArray) {
        log.debugf("TransitionRestClientWrapper.validateCanBeApplied productId: %d stateFromId: %s", productId, stateFromId);
        return service.getAllByProductAndStateFromAndRoles(productId, stateFromId);
    }

    @Override
    public List<Transition> getAllByStateInitialAndProduct(Long productId) {
        log.debugf("TransitionRestClientWrapper.validateCanBeApplied productId: %d", productId);
        return service.getAllByStateInitialAndProduct(productId);
    }

    @Override
    public List<Transition> getAllByStateInitialAndProductAndRoles(Long productId, JsonArray userRolesJsonArray) {
        log.debugf("TransitionRestClientWrapper.validateCanBeApplied productId: %d", productId);
        return service.getAllByStateInitialAndProductAndRoles(productId);
    }

    @Override
    public Boolean validateCanBeApplied(Long productId, String stateId, String transitionId) {
        log.debugf("TransitionRestClientWrapper.validateCanBeApplied productId: %d stateId: %s transitionId: %s", stateId, transitionId);
        return service.validateCanBeApplied(productId, stateId, transitionId);
    }
}