package io.samancore.client.rest.microprofile;

import io.samancore.model.Transition;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@ApplicationScoped
@RegisterRestClient(configKey = "workflow-api")
@RegisterClientHeaders
@Path("/states")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TransitionRestClient {

    @GET
    @Path("/get-by-product-and-transition/{productId}/{transitionId}")
    Uni<Transition> getByProductAndTransition(@PathParam("productId") Long productId, @PathParam("transitionId") String transitionId);

    @GET
    @Path("/get-all-by-product-and-state-from/{productId}/{statusFromId}")
    Uni<List<Transition>> getAllByProductAndStateFrom(@PathParam("productId") Long productId, @PathParam("statusFromId") String stateFromId);

    @GET
    @Path("/get-all-by-product-and-state-from-and-roles/{productId}/{stateFromId}")
    Uni<List<Transition>> getAllByProductAndStateFromAndRoles(@PathParam("productId") Long productId, @PathParam("stateFromId") String stateFromId);

    @GET
    @Path("/get-all-initial-by-product/{productId}")
    Uni<List<Transition>> getAllByStateInitialAndProduct(@PathParam("productId") Long productId);

    @GET
    @Path("/get-all-initial-by-product-and-roles/{productId}")
    Uni<List<Transition>> getAllByStateInitialAndProductAndRoles(@PathParam("productId") Long productId);

    @GET
    @Path("/validate-can-be-applied/{productId}/{stateId}/{transitionId}")
    Uni<Boolean> validateCanBeApplied(@PathParam("productId") Long productId, @PathParam("stateId") String stateId, @PathParam("transitionId") String transitionId);
}