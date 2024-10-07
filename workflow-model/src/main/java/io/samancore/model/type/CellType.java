package io.samancore.model.type;

import java.util.Arrays;

public enum CellType {
    LINK("standard.Link"),
    CIRCLE("standard.Circle"),
    RECTANGLE("standard.Rectangle");

    final String description;

    CellType(String description) {
        this.description = description;
    }

    public static CellType getByDescription(String description) {
        return Arrays.stream(values()).filter(cellType -> (cellType.getDescription().equalsIgnoreCase(description))).findAny().orElse(null);
    }

    public String getDescription() {
        return description;
    }
}
