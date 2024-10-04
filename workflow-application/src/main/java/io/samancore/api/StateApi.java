package io.samancore.api;

import io.samancore.model.State;
import io.samancore.service.StateService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@Path("/states")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StateApi {

    @Inject
    Logger log;

    @Inject
    StateService service;

    @GET
    @Path("/get-by-product-and-state/{productId}/{stateId}")
    @RolesAllowed({"official"})
    public Uni<State> getByProductAndStateId(@PathParam("productId") Long productId, @PathParam("stateId") String stateId) {
        log.debugf("StateApi.getByProductAndStateId productId %d stateId: %s", productId, stateId);
        return service.getByProductAndStateId(productId, stateId);
    }

    @GET
    @Path("/get-initial-state-by-product/{productId}")
    @RolesAllowed({"official"})
    public Uni<State> getInitialByProductId(@PathParam("productId") Long productId) {
        log.debugf("StateApi.getInitialByProductId productId: %s", productId);
        return service.getInitialByProductId(productId);
    }
}
