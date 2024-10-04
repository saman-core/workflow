package io.samancore.model;

import io.samancore.model.type.StateCategoryType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;


@Value
@Jacksonized
@Builder(
        setterPrefix = "set",
        builderMethodName = "newBuilder",
        toBuilder = true
)
public class State {
    @NotNull
    String id;
    StateCategoryType category;
    @NotNull
    String name;
    Boolean status;
    @NotNull
    Boolean isInitial;
    Workflow workflow;
    Long workflowId;
    List<StateRole> stateRoles;
}
