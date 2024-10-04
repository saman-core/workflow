package io.samancore.model.request;

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
public class Cell {
    String id;
    String name;
    String type;
    Object data;
    CellRelation source;
    CellRelation target;
    List<String> roles;
}
