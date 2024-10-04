package io.samancore.service.impl;

import io.samancore.model.State;
import io.samancore.service.StateService;
import io.samancore.util.GraphUtil;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class StateServiceImpl implements StateService {

    @Inject
    Logger log;

    @Inject
    GraphUtil graphUtil;

    @Override
    public Uni<State> getByProductAndStateId(Long productId, String stateId) {
        return graphUtil.getByProductAndStateId(productId, stateId);
    }

    @Override
    public Uni<State> getInitialByProductId(Long productId) {
        log.debugf("TransitionServiceImpl.createAll createStatusRequestList: %s", productId);
        return graphUtil.getInitialByProduct(productId);
    }
}
