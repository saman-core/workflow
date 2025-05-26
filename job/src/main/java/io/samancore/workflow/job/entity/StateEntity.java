package io.samancore.workflow.job.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "state")
public class StateEntity {

    @Id
    private UUID id;

    private String name;

    private String category;

    @Column(name = "is_initial")
    private Boolean isInitial;

    @ElementCollection
    @CollectionTable(name = "state_roles", joinColumns = @JoinColumn(name = "state_id"))
    @Column(name = "role")
    private List<String> stateRoles;
}