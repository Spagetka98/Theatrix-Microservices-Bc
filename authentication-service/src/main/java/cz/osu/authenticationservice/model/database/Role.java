package cz.osu.authenticationservice.model.database;

import cz.osu.authenticationservice.model.enums.ERole;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "Roles")
@Data
@NoArgsConstructor
public class Role {
    @Id
    private String id;

    @NotNull(message = "Role name cannot be null")
    private ERole name;

    public Role(ERole name){
        this.name = name;
    }
}