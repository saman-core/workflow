package io.samancore.init;

import io.quarkus.runtime.Startup;
import io.samancore.cache.WorkflowCache;
import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@Startup
public class AppInit {

    @Inject
    Logger log;

    @Inject
    WorkflowCache workflowCache;

    @PostConstruct
    public void init() {
        log.info("AppInit.init ");
        try {
            workflowCache.init();
        } catch (Exception e) {
            log.error("ERROR when init app workflow: ".concat(e.getMessage()), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.NOT_SPECIFIED_ERROR, e);
        }
    }
}
