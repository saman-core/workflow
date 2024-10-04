package io.samancore.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Getter
@Setter
@Table(name = "workflow")
public class WorkflowEntity {

    @Id
    @GeneratedValue(generator = "sequence-workflow")
    @GenericGenerator(
            name = "sequence-workflow",
            parameters = {
                    @Parameter(name = "sequence_name", value = "sq_workflow"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column
    private String description;

    @Column(nullable = false, unique = true)
    private Long productId;

}
