package io.samancore.workflow.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.Startup;
import io.samancore.workflow.json_diagram.WorkflowDiagram;
import io.samancore.workflow.model.State;
import io.samancore.workflow.model.StateCategoryType;
import io.samancore.workflow.model.Transition;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.samancore.workflow.app.util.GraphCache.GRAPH;

@Startup
@ApplicationScoped
public class LoadStartup {
    private static final Logger log = Logger.getLogger(LoadStartup.class);

    @ConfigProperty(name = "app.json.path")
    String jsonFilePath;

    @PostConstruct
    public void init() {
        log.info("INIT Graph loaded");

        try (InputStream inputStream = new FileInputStream(jsonFilePath)) {
            var objectMapper = new ObjectMapper();
            var erDiagram = objectMapper.readValue(inputStream, WorkflowDiagram.class);

            Map<String, State> entityMap = new HashMap<>();
            erDiagram.getCells().stream()
                    .filter(cell -> "standard.Circle".equals(cell.getType()))
                    .forEach(cell -> {
                        var entityModel = new State(
                                cell.getId(),
                                cell.getName(),
                                StateCategoryType.INITIAL,
                                true,
                                Collections.emptyList()
                        );
                        entityMap.put(cell.getId(), entityModel);
                        GRAPH.addVertex(entityModel);
                        log.infof("Vertex: %s", entityModel.name());
                    });

            erDiagram.getCells().stream()
                    .filter(cell -> "standard.Rectangle".equals(cell.getType()))
                    .forEach(cell -> {
                        var entityModel = new State(
                                cell.getId(),
                                cell.getName(),
                                cell.getStateType(),
                                false,
                                cell.getRoles()
                        );
                        entityMap.put(cell.getId(), entityModel);
                        GRAPH.addVertex(entityModel);
                        log.infof("Vertex: %s", entityModel.name());
                    });

            erDiagram.getCells().stream()
                    .filter(cell -> "standard.Link".equals(cell.getType()))
                    .forEach(link -> {
                        State source = entityMap.get(link.getSource().getId());
                        State target = entityMap.get(link.getTarget().getId());

                        if (source != null && target != null) {
                            var relationship = new Transition(
                                    link.getId(),
                                    link.getName(),
                                    source,
                                    target,
                                    link.getRoles(),
                                    link.getData()
                            );
                            GRAPH.addEdge(source, target, relationship);
                            log.infof("Edge: %s", relationship.name());
                        }
                    });

            log.info("Graph loaded successfully.");
        } catch (Exception e) {
            log.error("Error loading Graph", e);
        }
    }
}
