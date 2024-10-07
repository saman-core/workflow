package io.samancore.service;

import io.samancore.model.Transition;
import io.samancore.model.request.WorkflowCellsRequest;

import java.util.List;

public interface WorkFlowService {

    List<Transition> create(WorkflowCellsRequest request, Long productId);
}
