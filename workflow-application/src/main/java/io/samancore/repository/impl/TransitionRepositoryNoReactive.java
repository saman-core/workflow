package io.samancore.repository.impl;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.data.TransitionEntity;
import io.samancore.repository.TransitionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class TransitionRepositoryNoReactive implements TransitionRepository {

    @Inject
    Logger log;

    @Inject
    EntityManager entityManager;

    @Override
    public List<TransitionEntity> getAll() {
        log.debugf("TransitionRepositoryReactive.getAll");
        try {
            return entityManager.createQuery(" FROM TransitionEntity t", TransitionEntity.class).getResultList();
        } catch (Exception e) {
            log.error("error getAll", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }
}
