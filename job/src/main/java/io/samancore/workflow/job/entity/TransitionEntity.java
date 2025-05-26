package io.samancore.workflow.job.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "transition")
public class TransitionEntity {

    @Id
    private UUID id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "source_state_id")
    private StateEntity sourceState;

    @ManyToOne
    @JoinColumn(name = "target_state_id")
    private StateEntity targetState;

    @ElementCollection
    @CollectionTable(name = "transition_roles", joinColumns = @JoinColumn(name = "transition_id"))
    @Column(name = "role")
    private List<String> allowedRoles;
}