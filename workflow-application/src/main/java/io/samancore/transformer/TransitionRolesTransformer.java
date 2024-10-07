package io.samancore.transformer;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.transformer.GenericTransformer;
import io.samancore.data.TransitionRoleEntity;
import io.samancore.model.TransitionRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TransitionRolesTransformer extends GenericTransformer<TransitionRoleEntity, TransitionRole> {

    @Inject
    Logger log;

    public TransitionRoleEntity toEntity(TransitionRole model) {
        try {
            log.debugf("TransitionRolesTransformer.toEntity model: %s", model);
            return this.transformToEntity(model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public TransitionRole toModel(TransitionRoleEntity entity) {
        try {
            log.debugf("TransitionRolesTransformer.toModel entity: %s", entity);
            return this.transformToModel(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public TransitionRoleEntity copyToAttached(TransitionRoleEntity detached, TransitionRoleEntity attached) {
        try {
            log.debugf("TransitionRolesTransformer.copyToAttached detached: %s attached: %s", detached, attached);
            return transformCopyToAttached(detached, attached);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }
}
