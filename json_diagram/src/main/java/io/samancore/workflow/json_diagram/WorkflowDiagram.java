package io.samancore.workflow.json_diagram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.samancore.workflow.model.StateCategoryType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowDiagram {
    private List<Cell> cells;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cell {
        private String type;
        private String id;
        private String name;
        private StateCategoryType stateType;
        private List<String> roles;
        private Source source;
        private Target target;
        private Map<String, Object> data;
    }

    @Data
    public static class Source {
        private String id;
    }

    @Data
    public static class Target {
        private String id;
    }
}
