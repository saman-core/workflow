package io.samancore.transformer;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.transformer.GenericTransformer;
import io.samancore.data.StateRoleEntity;
import io.samancore.model.StateRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class StateRoleTransformer extends GenericTransformer<StateRoleEntity, StateRole> {

    @Inject
    Logger log;

    public StateRoleEntity toEntity(StateRole model) {
        try {
            log.debugf("StatusRolesTransformer.toEntity model: %s", model);
            return this.transformToEntity(model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public StateRole toModel(StateRoleEntity entity) {
        try {
            log.debugf("StatusRolesTransformer.toModel entity: %s", entity);
            return this.transformToModel(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public StateRoleEntity copyToAttached(StateRoleEntity detached, StateRoleEntity attached) {
        try {
            log.debugf("StatusRolesTransformer.copyToAttached detached: %s attached: %s", detached, attached);
            return transformCopyToAttached(detached, attached);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }
}
