package io.samancore.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Getter
@Setter
@Table(name = "transition_role")
public class TransitionRoleEntity {

    @Id
    @GeneratedValue(generator = "sequence-transition_role")
    @GenericGenerator(
            name = "sequence-transition_role",
            parameters = {
                    @Parameter(name = "sequence_name", value = "sq_transition_role"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(nullable = false)
    private String role;

    @Column(name = "transition_id")
    private String transitionId;
}
