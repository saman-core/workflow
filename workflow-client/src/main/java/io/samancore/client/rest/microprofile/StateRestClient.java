package io.samancore.client.rest.microprofile;

import io.samancore.model.State;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@RegisterRestClient(configKey = "workflow-api")
@RegisterClientHeaders
@Path("/states")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface StateRestClient {

    @GET
    @Path("/get-by-product-and-state/{productId}/{stateId}")
    Uni<State> getByProductAndStateId(@PathParam("productId") Long productId, @PathParam("stateId") String stateId);

    @GET
    @Path("/get-initial-state-by-product/{productId}")
    Uni<State> getInitialByProductId(@PathParam("productId") Long productId);
}