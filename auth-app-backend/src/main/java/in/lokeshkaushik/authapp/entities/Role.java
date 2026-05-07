package in.lokeshkaushik.authapp.entities;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @PrePersist
    public void generateId() {
        if(id == null) {
            id = Generators.timeBasedEpochGenerator().generate();
        }
    }
}
