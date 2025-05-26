package io.samancore.workflow.job.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.samancore.workflow.json_diagram.WorkflowDiagram;
import io.samancore.workflow.job.entity.StateEntity;
import io.samancore.workflow.job.entity.TransitionEntity;
import io.samancore.workflow.model.StateCategoryType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class JsonProcess {
    private static final Logger log = Logger.getLogger(JsonProcess.class);

    @ConfigProperty(name = "app.json.path")
    String jsonFilePath;

    @Inject
    private EntityManager entityManager;

    @Inject
    ObjectMapper objectMapper;

    @Transactional
    public void run() {
        log.info("INIT load json.");
        try (InputStream inputStream = new FileInputStream(jsonFilePath)) {
            var erDiagram = objectMapper.readValue(inputStream, WorkflowDiagram.class);

            deleteAllTransitions();
            deleteAllStates();

            Map<String, StateEntity> entityMap = new HashMap<>();
            erDiagram.getCells().stream()
                    .filter(cell -> "standard.Circle".equals(cell.getType()))
                    .forEach(cell -> {
                        StateEntity state = new StateEntity();
                        state.setId(UUID.fromString(cell.getId()));
                        state.setName(cell.getName());
                        state.setCategory(StateCategoryType.INITIAL.getValue());
                        state.setIsInitial(true);
                        state.setStateRoles(Collections.emptyList());
                        persistState(state);
                        entityMap.put(cell.getId(), state);
                        log.infof("Persist state %s", state.getName());
                    });
            erDiagram.getCells().stream()
                    .filter(cell -> "standard.Rectangle".equals(cell.getType()))
                    .forEach(cell -> {
                        StateEntity state = new StateEntity();
                        state.setId(UUID.fromString(cell.getId()));
                        state.setName(cell.getName());
                        state.setCategory(cell.getStateType().getValue());
                        state.setIsInitial(false);
                        state.setStateRoles(cell.getRoles());
                        persistState(state);
                        entityMap.put(cell.getId(), state);
                        log.infof("Persist state %s", state.getName());
                    });

            erDiagram.getCells().stream()
                    .filter(cell -> "standard.Link".equals(cell.getType()))
                    .forEach(link -> {
                        TransitionEntity relationship = new TransitionEntity();
                        relationship.setId(UUID.fromString(link.getId()));
                        relationship.setName(link.getName());
                        relationship.setSourceState(entityMap.get(link.getSource().getId()));
                        relationship.setTargetState(entityMap.get(link.getTarget().getId()));
                        relationship.setAllowedRoles(link.getRoles());
                        persistTransition(relationship);
                        log.infof("Persist relationship %s", relationship.getName());
                    });
            log.info("Graph loaded successfully.");
        } catch (Exception e) {
            log.error("Error loading Graph", e);
            throw new IllegalStateException("Error loading Graph");
        }
    }

    public void persistState(StateEntity state) {
        entityManager.persist(state);
    }

    public void deleteAllStates() {
        entityManager.createQuery("DELETE FROM StateEntity").executeUpdate();
    }

    public void persistTransition(TransitionEntity transition) {
        entityManager.persist(transition);
    }

    public void deleteAllTransitions() {
        entityManager.createQuery("DELETE FROM TransitionEntity").executeUpdate();
    }
}
