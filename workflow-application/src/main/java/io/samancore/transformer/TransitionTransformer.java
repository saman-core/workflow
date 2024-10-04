package io.samancore.transformer;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.transformer.GenericTransformer;
import io.samancore.data.StateEntity;
import io.samancore.data.TransitionEntity;
import io.samancore.data.TransitionRoleEntity;
import io.samancore.model.State;
import io.samancore.model.Transition;
import io.samancore.model.TransitionRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.function.Function;

@ApplicationScoped
public class TransitionTransformer extends GenericTransformer<TransitionEntity, Transition> {

    @Inject
    Logger log;

    @Inject
    StateTransformer stateTransformer;

    @Inject
    TransitionRolesTransformer transitionRolesTransformer;

    public TransitionEntity toEntity(Transition model) {
        try {
            log.debugf("TransitionTransformer.toEntity model: %s", model);
            var pairTransitionRolesToEntity = Pair.of("transitionRoles", (Function<List<TransitionRole>, ?>) transitionRolesTransformer::toEntityList);
            var pairStateFromEntity = Pair.of("stateFrom", (Function<State, ?>) stateTransformer::toEntity);
            var pairStateToEntity = Pair.of("stateTo", (Function<State, ?>) stateTransformer::toEntity);
            return this.transformToEntity(model, pairTransitionRolesToEntity, pairStateFromEntity, pairStateToEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public Transition toModel(TransitionEntity entity) {
        try {
            log.debugf("TransitionTransformer.toModel entity: %s", entity);
            var pairTransitionRolesToModel = Pair.of("transitionRoles", (Function<List<TransitionRoleEntity>, ?>) transitionRolesTransformer::toModelList);
            var pairStateFromToModel = Pair.of("stateFrom", (Function<StateEntity, ?>) stateTransformer::toModel);
            var pairStateToModel = Pair.of("stateTo", (Function<StateEntity, ?>) stateTransformer::toModel);
            return this.transformToModel(entity, pairTransitionRolesToModel, pairStateFromToModel, pairStateToModel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public List<Transition> toModelList(List<TransitionEntity> entities) {
        try {
            log.debugf("TransitionTransformer.toModelList entities.size: %s", entities.size());
            return entities.stream().map(this::toModel).toList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public List<TransitionEntity> toEntityList(List<Transition> models) {
        try {
            log.debugf("TransitionTransformer.toEntityList entities.size: %s", models.size());
            return models.stream().map(this::toEntity).toList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }


    public TransitionEntity copyToAttached(TransitionEntity detached, TransitionEntity attached) {
        try {
            log.debugf("TransitionTransformer.copyToAttached detached: %s attached: %s", detached, attached);
            return transformCopyToAttached(detached, attached);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }
}
