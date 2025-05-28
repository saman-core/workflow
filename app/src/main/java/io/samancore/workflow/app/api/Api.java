package io.samancore.workflow.app.api;

import io.samancore.workflow.app.util.GraphQueries;
import io.samancore.workflow.model.State;
import io.samancore.workflow.model.Transition;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.List;

@Path("")
public class Api {

    @GET
    @Path("{product}/state/{stateId}")
    @RolesAllowed({"admin"})
    public State getState(@PathParam("product") String product, @PathParam("stateId") String stateId) {
        return GraphQueries.getVertex(product, stateId);
    }

    @GET
    @Path("{product}/transition/{transitionId}")
    @RolesAllowed({"admin"})
    public Transition getTransition(@PathParam("product") String product, @PathParam("transitionId") String transitionId) {
        return GraphQueries.getEdge(product, transitionId);
    }

    @GET
    @Path("{product}/state/{stateId}/transitions")
    @RolesAllowed({"admin"})
    public List<Transition> getTransitionsByState(@PathParam("product") String product, @PathParam("stateId") String stateId) {
        return GraphQueries.getEdgesFromVertex(product, stateId);
    }

    @GET
    @Path("{product}/initial-transitions")
    @RolesAllowed({"admin"})
    public List<Transition> getInitialTransitions(@PathParam("product") String product) {
        return GraphQueries.getInitialEdges(product);
    }
}
