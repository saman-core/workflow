package io.samancore.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "transition")
public class TransitionEntity {

    @Id
    @Column(name = "id", unique = true)
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "state_from_id", referencedColumnName = "id", updatable = false, insertable = false)
    private StateEntity stateFrom;

    @Column(name = "state_from_id", nullable = false)
    private String stateFromId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "state_to_id", referencedColumnName = "id", updatable = false, insertable = false)
    private StateEntity stateTo;

    @Column(name = "state_to_id", nullable = false)
    private String stateToId;

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "transition_id")
    private List<TransitionRoleEntity> transitionRoles;
}
