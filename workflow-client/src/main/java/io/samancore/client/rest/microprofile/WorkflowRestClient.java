package io.samancore.client.rest.microprofile;

import io.samancore.model.Transition;
import io.samancore.model.request.WorkflowCellsRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@ApplicationScoped
@RegisterRestClient(configKey = "workflow-api")
@RegisterClientHeaders
@Path("/workflows")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface WorkflowRestClient {

    @POST
    @Path("/{productId}")
    List<Transition> create(WorkflowCellsRequest request, @PathParam("productId") Long productId);
}