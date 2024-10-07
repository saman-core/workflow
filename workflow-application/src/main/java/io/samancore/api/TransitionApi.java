package io.samancore.api;

import io.samancore.model.Transition;
import io.samancore.service.TransitionService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/transitions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransitionApi {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";

    @Inject
    Logger log;

    @Inject
    TransitionService service;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/get-by-product-and-transition/{productId}/{transitionId}")
    @RolesAllowed({"official"})
    public Uni<Transition> getByProductAndTransition(@PathParam("productId") Long productId, @PathParam("transitionId") String transitionId) {
        log.debugf("TransitionApi.getById productId %d transitionId: %s", productId, transitionId);
        return service.getByProductAndTransition(productId, transitionId);
    }

    @GET
    @Path("/get-all-by-product-and-state-from/{productId}/{statusFromId}")
    @RolesAllowed({"official"})
    public Uni<List<Transition>> getAllByProductAndStateFrom(@PathParam("productId") Long productId, @PathParam("statusFromId") String stateFromId) {
        log.debugf("TransitionApi.getAllByProductAndStateFrom %d stateFromId: %s", productId, stateFromId);
        return service.getAllByProductAndStateFrom(productId, stateFromId);
    }

    @GET
    @Path("/get-all-by-product-and-state-from-and-roles/{productId}/{stateFromId}")
    @RolesAllowed({"official"})
    public Uni<List<Transition>> getAllByProductAndStateFromAndRoles(@PathParam("productId") Long productId, @PathParam("stateFromId") String stateFromId) {
        log.debugf("TransitionApi.getAllByProductAndStateFromAndRoles productId %d stateFromId: %s", productId, stateFromId);
        var userRolesJsonArray = ((JsonObject) jwt.getClaim(REALM_ACCESS)).getJsonArray(ROLES);
        return service.getAllByProductAndStateFromAndRoles(productId, stateFromId, userRolesJsonArray);
    }

    @GET
    @Path("/get-all-initial-by-product/{productId}")
    @RolesAllowed({"official"})
    public Uni<List<Transition>> getAllByStateInitialAndProduct(@PathParam("productId") Long productId) {
        log.debugf("TransitionApi.getAllByStatusFrom statusFromId: %s", productId);
        return service.getAllByStateInitialAndProduct(productId);
    }

    @GET
    @Path("/get-all-initial-by-product-and-roles/{productId}")
    @RolesAllowed({"official"})
    public Uni<List<Transition>> getAllByStateInitialAndProductAndRoles(@PathParam("productId") Long productId) {
        log.debugf("TransitionApi.getAllInitialByProductAndRoles productId: %s", productId);
        var userRolesJsonArray = ((JsonObject) jwt.getClaim(REALM_ACCESS)).getJsonArray(ROLES);
        return service.getAllByStateInitialAndProductAndRoles(productId, userRolesJsonArray);
    }

    @GET
    @Path("/validate-can-be-applied/{productId}/{stateId}/{transitionId}")
    @RolesAllowed({"official"})
    public Uni<Boolean> validateCanBeApplied(@PathParam("productId") Long productId, @PathParam("stateId") String stateId, @PathParam("transitionId") String transitionId) {
        log.debugf("TransitionApi.validateCanBeApplied productId: %d stateId: %s transitionId: %s", productId, stateId, transitionId);
        return service.validateCanBeApplied(productId, stateId, transitionId);
    }
}
