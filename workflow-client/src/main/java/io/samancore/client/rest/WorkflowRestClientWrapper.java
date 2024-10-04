package io.samancore.client.rest;

import io.samancore.client.WorkflowClient;
import io.samancore.client.rest.microprofile.WorkflowRestClient;
import io.samancore.model.Transition;
import io.samancore.model.request.WorkflowCellsRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class WorkflowRestClientWrapper implements WorkflowClient {

    @Inject
    Logger log;

    @Inject
    @RestClient
    WorkflowRestClient service;

    @Override
    public List<Transition> create(WorkflowCellsRequest request, Long productId) {
        log.debugf("WorkflowRestClientWrapper.create %s  productid %d", request, productId);
        return service.create(request, productId);
    }
}