package io.samancore.transformer;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.transformer.GenericTransformer;
import io.samancore.data.StateEntity;
import io.samancore.data.StateRoleEntity;
import io.samancore.data.WorkflowEntity;
import io.samancore.model.State;
import io.samancore.model.StateRole;
import io.samancore.model.Workflow;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class StateTransformer extends GenericTransformer<StateEntity, State> {

    @Inject
    Logger log;

    @Inject
    StateRoleTransformer stateRoleTransformer;

    @Inject
    WorkFlowTransformer workFlowTransformer;

    public StateEntity toEntity(State model) {
        try {
            log.debugf("StatusTransformer.toEntity model: %s", model);
            var pairStateRoleToEntity = Pair.of("stateRoles", (Function<List<StateRole>, ?>) stateRoleTransformer::toEntityList);
            var pairWorkflowToEntity = Pair.of("workflow", (Function<Workflow, ?>) workFlowTransformer::toEntity);
            return this.transformToEntity(model, pairStateRoleToEntity, pairWorkflowToEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public State toModel(StateEntity entity) {
        try {
            log.debugf("StatusTransformer.toModel entity: %s", entity);
            var pairStateRoleToModel = Pair.of("stateRoles", (Function<List<StateRoleEntity>, ?>) stateRoleTransformer::toModelList);
            var pairWorkflowToModel = Pair.of("workflow", (Function<WorkflowEntity, ?>) workFlowTransformer::toModel);
            return this.transformToModel(entity, pairStateRoleToModel, pairWorkflowToModel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public List<State> toModelList(List<StateEntity> entities) {
        try {
            log.debugf("StatusTransformer.toModelList entities.size: %s", entities.size());
            return entities.stream().map(this::toModel).toList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public List<StateEntity> toEntityList(List<State> models) {
        try {
            log.debugf("StatusTransformer.toEntityList entities.size: %s", models.size());
            return models.stream().map(this::toEntity).toList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public StateEntity copyToAttached(StateEntity detached, StateEntity attached) {
        try {
            log.debugf("StatusTransformer.copyToAttached detached: %s attached: %s", detached, attached);
            return transformCopyToAttached(detached, attached);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }
}
