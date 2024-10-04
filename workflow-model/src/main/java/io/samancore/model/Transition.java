package io.samancore.model;

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
public class Transition {
    @NotNull
    String id;
    @NotNull
    State stateFrom;
    @NotNull
    State stateTo;
    @NotNull
    String stateFromId;
    @NotNull
    String stateToId;
    @NotNull
    String name;
    List<TransitionRole> transitionRoles;
}
