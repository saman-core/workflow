package io.samancore.client;

import io.samancore.model.Transition;
import io.samancore.model.request.WorkflowCellsRequest;

import java.util.List;

public interface WorkflowClient {

    List<Transition> create(WorkflowCellsRequest request, Long productId);
}