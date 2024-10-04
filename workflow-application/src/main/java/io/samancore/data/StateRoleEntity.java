package io.samancore.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Getter
@Setter
@Table(name = "state_role")
public class StateRoleEntity {

    @Id
    @GeneratedValue(generator = "sequence-state_role")
    @GenericGenerator(
            name = "sequence-state_role",
            parameters = {
                    @Parameter(name = "sequence_name", value = "sq_state_role"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(name = "state_id")
    private String stateId;

    @Column(nullable = false)
    private String role;
}
