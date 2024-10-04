package io.samancore.repository;

import io.samancore.data.StateEntity;
import io.samancore.data.TransitionEntity;
import io.samancore.data.WorkflowEntity;

import java.util.List;

public interface WorkflowRepository {

    List<TransitionEntity> create(WorkflowEntity entity, List<StateEntity> stateEntityList, List<TransitionEntity> transitionEntityList);

    WorkflowEntity getByProductId(Long productId);

    List<TransitionEntity> update(Long productId, List<StateEntity> stateEntityList, List<TransitionEntity> transitionEntityList);
}
