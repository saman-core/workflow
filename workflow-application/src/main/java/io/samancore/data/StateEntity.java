package io.samancore.data;

import io.samancore.model.type.StateCategoryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "state")
public class StateEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column
    private StateCategoryType category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private Boolean isInitial;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "workflow_id", referencedColumnName = "id", insertable = false, updatable = false)
    private WorkflowEntity workflow;

    @Column(name = "workflow_id", nullable = false)
    private Long workflowId;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    private List<StateRoleEntity> stateRoles;
}
