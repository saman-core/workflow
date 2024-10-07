package io.samancore.repository.impl;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.data.StateEntity;
import io.samancore.data.TransitionEntity;
import io.samancore.data.WorkflowEntity;
import io.samancore.repository.WorkflowRepository;
import io.samancore.transformer.StateTransformer;
import io.samancore.transformer.TransitionTransformer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class WorkflowRepositoryReactive implements WorkflowRepository {
    public static final String DELETE_STATE_ENTITY = "DELETE FROM StateEntity s WHERE s.id not in (:entityIds)";
    public static final String DELETE_TRANSITION_ENTITY = "DELETE FROM TransitionEntity t WHERE t.id not in (:entityIds)";
    public static final String DELETE_STATE_ROLE_ENTITY = "DELETE FROM StateRoleEntity s WHERE s.stateId not in (:entityIds) or s.stateId is null";
    public static final String DELETE_TRANSITION_ROLE_ENTITY = "DELETE FROM TransitionRoleEntity t WHERE t.transitionId not in (:entityIds) or t.transitionId is null";
    public static final String PARAMETER_KEY_ENTITY_IDS = "entityIds";

    @Inject
    Logger log;

    @Inject
    EntityManager entityManager;

    @Inject
    StateTransformer stateTransformer;

    @Inject
    TransitionTransformer transitionTransformer;

    @Override
    public List<TransitionEntity> create(WorkflowEntity entity, List<StateEntity> stateEntityList, List<TransitionEntity> transitionEntityList) {
        log.debugf("WorkflowRepositoryReactive.create entity: %s stateEntityList: %s transitionEntityList: %s ", entity, stateEntityList, transitionEntityList);
        try {
            return getTransitionEntities(entity, stateEntityList, transitionEntityList);
        } catch (Exception e) {
            log.error(" ERROR WorkflowRepositoryReactive.create", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    private List<TransitionEntity> getTransitionEntities(WorkflowEntity entity, List<StateEntity> stateEntityList, List<TransitionEntity> transitionEntityList) {
        entity.setId(null);
        entityManager.persist(entity);
        var stateChanged = changeWorkflowInStates(entity, stateEntityList);
        stateChanged.forEach(stateEntity -> entityManager.persist(stateEntity));
        var stateMap = stateChanged.stream().collect(Collectors.toMap(StateEntity::getId, stateEntity -> stateEntity));
        var transitionChanged = changeStateInTransitions(stateMap, transitionEntityList);
        transitionChanged.forEach(transitionEntity -> entityManager.persist(transitionEntity));
        return transitionChanged;
    }

    @Override
    public WorkflowEntity getByProductId(Long productId) {
        log.debugf("WorkflowRepositoryReactive.getByProductId %d ", productId);
        try {
            return entityManager.createQuery(" FROM WorkflowEntity w where w.productId = :productId", WorkflowEntity.class)
                    .setParameter("productId", productId)
                    .getResultStream().findAny().orElse(null);
        } catch (Exception e) {
            log.error("ERROR WorkflowRepositoryReactive.getByProductId", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public List<TransitionEntity> update(Long productId, List<StateEntity> stateEntityList, List<TransitionEntity> transitionEntityList) {
        log.debugf("WorkflowRepositoryReactive.update productId: %d stateEntityList: %s transitionEntityList: %s", productId, stateEntityList, transitionEntityList);
        try {
            var stateEntityMap = stateEntityList.stream().collect(Collectors.toMap(StateEntity::getId, entity -> entity));
            var transitionEntityMap = transitionEntityList.stream().collect(Collectors.toMap(TransitionEntity::getId, entity -> entity));
            var statesUpdated = createAndUpdateState(stateEntityMap);
            createAndUpdateTransition(transitionEntityList, transitionEntityMap, statesUpdated);
            executeUpdate(DELETE_TRANSITION_ROLE_ENTITY, transitionEntityMap.keySet());
            executeUpdate(DELETE_STATE_ROLE_ENTITY, stateEntityMap.keySet());
            executeUpdate(DELETE_TRANSITION_ENTITY, transitionEntityMap.keySet());
            executeUpdate(DELETE_STATE_ENTITY, stateEntityMap.keySet());
            return transitionEntityMap.values().stream().toList();
        } catch (Exception e) {
            log.error(" ERROR WorkflowRepositoryReactive.create", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    private Integer executeUpdate(String sql, Set<String> entityIds) {
        log.debugf("WorkflowRepositoryReactive.executeUpdate sql: %s entityIds: %s ", sql, entityIds);
        return entityManager.createQuery(sql)
                .setParameter(PARAMETER_KEY_ENTITY_IDS, entityIds)
                .executeUpdate();
    }

    private List<TransitionEntity> createAndUpdateTransition(List<TransitionEntity> transitionEntityList, Map<String, TransitionEntity> transitionEntityMap, List<StateEntity> stateEntities) {
        log.debugf("WorkflowRepositoryReactive.createAndUpdateTransition transitionEntityList: %s transitionEntityMap: %s ", transitionEntityList, transitionEntityMap);
        var stateEntitiesMap = stateEntities.stream().collect(Collectors.toMap(StateEntity::getId, entity -> entity));
        var transitionEntities = changeStateInTransitions(stateEntitiesMap, transitionEntityList);
        transitionEntities.forEach(transitionEntity -> transitionEntityMap.put(transitionEntity.getId(), transitionEntity));
        var transitionAttached = entityManager.createQuery(" FROM TransitionEntity t where t.id in (:transitionId)", TransitionEntity.class)
                .setParameter("transitionId", transitionEntityMap.keySet())
                .getResultList();
        transitionAttached.forEach(attached -> transitionEntityMap.put(attached.getId(), transitionTransformer.copyToAttached(transitionEntityMap.get(attached.getId()), attached)));
        transitionEntityMap.values().forEach(transitionEntity -> entityManager.persist(transitionEntity));
        return transitionEntityMap.values().stream().toList();
    }

    private List<StateEntity> createAndUpdateState(Map<String, StateEntity> stateEntityMap) {
        log.debugf("WorkflowRepositoryReactive.createAndUpdateState stateEntityMap: %s ", stateEntityMap);
        var stateAttached = entityManager.createQuery("FROM StateEntity s where s.id in (:stateIds)", StateEntity.class)
                .setParameter("stateIds", stateEntityMap.keySet())
                .getResultList();
        stateAttached.forEach(attached -> stateEntityMap.put(attached.getId(), stateTransformer.copyToAttached(stateEntityMap.get(attached.getId()), attached)));
        stateEntityMap.values().forEach(stateEntity -> entityManager.persist(stateEntity));
        return stateEntityMap.values().stream().toList();
    }

    private List<StateEntity> changeWorkflowInStates(WorkflowEntity workflowEntity, List<StateEntity> stateEntityList) {
        log.debugf("WorkflowRepositoryReactive.changeWorkflowInStates workflowEntity: %s stateEntityList %s", workflowEntity, stateEntityList);
        stateEntityList.forEach(stateEntity -> {
            stateEntity.setWorkflow(workflowEntity);
            stateEntity.setWorkflowId(workflowEntity.getId());
        });
        return stateEntityList;
    }

    private List<TransitionEntity> changeStateInTransitions(Map<String, StateEntity> stateEntitiesMap, List<TransitionEntity> transitionEntityList) {
        log.debugf("WorkflowRepositoryReactive.changeStateInTransitions stateEntitiesMap: %s transitionEntityList: %s", stateEntitiesMap, transitionEntityList);
        transitionEntityList.forEach(transitionEntity -> {
            transitionEntity.setStateFrom(stateEntitiesMap.get(transitionEntity.getStateFrom().getId()));
            transitionEntity.setStateTo(stateEntitiesMap.get(transitionEntity.getStateTo().getId()));
        });
        return transitionEntityList;
    }
}
