package io.samancore.api;


import io.samancore.model.Transition;
import io.samancore.model.request.WorkflowCellsRequest;
import io.samancore.service.WorkFlowService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/workflows")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WorkflowApi {

    @Inject
    Logger log;

    @Inject
    WorkFlowService service;

    @POST
    @Path("/{productId}")
    @RolesAllowed({"official"})
    public List<Transition> create(WorkflowCellsRequest request, @PathParam("productId") Long productId) {
        log.debugf("WorkflowApi.create request: %s", request);
        return service.create(request, productId);
    }
}
