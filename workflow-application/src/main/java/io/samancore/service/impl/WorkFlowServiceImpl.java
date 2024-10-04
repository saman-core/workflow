package io.samancore.service.impl;

import io.samancore.common.error.exception.BusinessException;
import io.samancore.common.error.message.BusinessExceptionsEnum;
import io.samancore.data.StateEntity;
import io.samancore.data.TransitionEntity;
import io.samancore.data.WorkflowEntity;
import io.samancore.model.*;
import io.samancore.model.request.Cell;
import io.samancore.model.request.WorkflowCellsRequest;
import io.samancore.model.type.CellType;
import io.samancore.repository.WorkflowRepository;
import io.samancore.service.WorkFlowService;
import io.samancore.transformer.StateTransformer;
import io.samancore.transformer.TransitionTransformer;
import io.samancore.transformer.WorkFlowTransformer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class WorkFlowServiceImpl implements WorkFlowService {

    @Inject
    Logger log;

    @Inject
    WorkflowRepository repository;

    @Inject
    WorkFlowTransformer transformer;

    @Inject
    StateTransformer stateTransformer;

    @Inject
    TransitionTransformer transitionTransformer;

    @Transactional
    @Override
    public List<Transition> create(WorkflowCellsRequest request, Long productId) {
        log.debugf("WorkFlowServiceImpl.create request: %s productId: %d", request, productId);
        var workflowEntity = repository.getByProductId(productId);
        List<TransitionEntity> result = null;
        if (workflowEntity == null) {
            result = repository.create(getWorkflowEntity(request, productId), getStateEntity(request), getTransitionEntity(request));
        } else {
            result = repository.update(productId, getStateEntity(request, workflowEntity), getTransitionEntity(request));
        }
        return transitionTransformer.toModelList(result);
    }

    private WorkflowEntity getWorkflowEntity(WorkflowCellsRequest request, Long productId) {
        log.debugf("WorkFlowServiceImpl.getWorkflowEntity request: %s productId: %d", request, productId);
        var model = Workflow.newBuilder()
                .setDescription(request.getDescription())
                .setProductId(productId)
                .build();
        validateModel(model);
        return transformer.toEntity(model);
    }

    private List<StateEntity> getStateEntity(WorkflowCellsRequest request) {
        log.debugf("WorkFlowServiceImpl.getStateEntity request: %s", request);
        return request.getCells().stream()
                .filter(cell -> !CellType.getByDescription(cell.getType()).equals(CellType.LINK))
                .map(cell -> getBuildState(cell, null))
                .map(state -> stateTransformer.toEntity(state)).collect(Collectors.toList());
    }

    private List<StateEntity> getStateEntity(WorkflowCellsRequest request, WorkflowEntity workflowEntity) {
        log.debugf("WorkFlowServiceImpl.getStateEntity request: %s workflowEntity: %s", request, workflowEntity);
        return request.getCells().stream()
                .filter(cell -> !CellType.getByDescription(cell.getType()).equals(CellType.LINK))
                .map(cell -> getBuildState(cell, workflowEntity))
                .map(state -> stateTransformer.toEntity(state)).collect(Collectors.toList());
    }

    private State getBuildState(Cell cell, WorkflowEntity workflowEntity) {
        log.debugf("WorkFlowServiceImpl.getBuildState cell: %s workflowEntity: %s", cell, workflowEntity);
        var model = State.newBuilder()
                .setId(cell.getId())
                .setName(cell.getName())
                .setWorkflow(workflowEntity != null ? transformer.toModel(workflowEntity) : null)
                .setWorkflowId(workflowEntity != null ? workflowEntity.getId() : null)
                .setStatus(true)
                .setIsInitial(CellType.getByDescription(cell.getType()).equals(CellType.CIRCLE))
                .setStateRoles(cell.getRoles() != null ? cell.getRoles()
                        .stream()
                        .map(role -> getBuildStateRole(cell, role))
                        .collect(Collectors.toList())
                        : null)
                .build();
        validateModel(model);
        return model;
    }

    private StateRole getBuildStateRole(Cell cell, String role) {
        log.debugf("WorkFlowServiceImpl.getBuildStateRole cell: %s role: %s", cell, role);
        var model = StateRole.newBuilder()
                .setStateId(cell.getId())
                .setRole(role)
                .build();
        validateModel(model);
        return model;
    }

    private List<TransitionEntity> getTransitionEntity(WorkflowCellsRequest request) {
        log.debugf("WorkFlowServiceImpl.getTransitionEntity request: %s", request);
        return request.getCells().stream()
                .filter(cell -> CellType.getByDescription(cell.getType()).equals(CellType.LINK))
                .map(this::getBuildTransition)
                .map(transition -> transitionTransformer.toEntity(transition)).collect(Collectors.toList());
    }

    private Transition getBuildTransition(Cell cell) {
        log.debugf("WorkFlowServiceImpl.getBuildTransition cell: %s", cell);
        var model = Transition.newBuilder()
                .setId(cell.getId())
                .setName(cell.getName())
                .setStateFrom(State.newBuilder().setId(cell.getSource().getId()).build())
                .setStateFromId(cell.getSource().getId())
                .setStateTo(State.newBuilder().setId(cell.getTarget().getId()).build())
                .setStateToId(cell.getTarget().getId())
                .setTransitionRoles(cell.getRoles()
                        .stream()
                        .map(role -> getBuildTransitionRole(cell, role))
                        .collect(Collectors.toList()))
                .build();
        validateModel(model);
        return model;
    }

    private TransitionRole getBuildTransitionRole(Cell cell, String role) {
        log.debugf("WorkFlowServiceImpl.getBuildTransitionRole cell: %s role: %s", cell, role);
        return TransitionRole.newBuilder()
                .setTransitionId(cell.getId())
                .setRole(role)
                .build();
    }

    private void validateModel(Object model) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        var validator = factory.getValidator();
        var violations = validator.validate(model);
        if (!violations.isEmpty()) {
            var errorMessage = violations.stream().map(constraintViolation -> constraintViolation.getRootBeanClass().toString().concat(".").concat(constraintViolation.getPropertyPath().toString()).concat(": ").concat(constraintViolation.getMessage())).collect(Collectors.joining(", "));
            throw new BusinessException(BusinessExceptionsEnum.VALIDATE_REQUEST_ERROR, List.of(errorMessage));
        }
    }
}
