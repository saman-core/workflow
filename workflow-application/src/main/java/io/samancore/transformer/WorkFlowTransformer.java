package io.samancore.transformer;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.transformer.GenericTransformer;
import io.samancore.data.WorkflowEntity;
import io.samancore.model.Workflow;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class WorkFlowTransformer extends GenericTransformer<WorkflowEntity, Workflow> {

    @Inject
    Logger log;

    public WorkflowEntity toEntity(Workflow model) {
        try {
            log.debugf("WorkFlowTransformer.toEntity model: %s", model);
            return this.transformToEntity(model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public Workflow toModel(WorkflowEntity entity) {
        try {
            log.debugf("WorkFlowTransformer.toModel entity: %s", entity);
            return this.transformToModel(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }
}
