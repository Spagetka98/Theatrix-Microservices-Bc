package cz.osu.authenticationservice.model.database;

import cz.osu.authenticationservice.model.database.embedded.AccessToken;
import cz.osu.authenticationservice.model.database.embedded.RefreshToken;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Document(collection = "Users")
@Data
@ToString(of = { "id", "username","email","roles", })
public class AppUser {
    @Id
    private String id;

    @NotBlank(message = "Username of AppUser cannot be blank")
    @Size(min = 5, max = 25,message = "Minimum size of username is 5 and Maximum size of username is 25")
    @Indexed(name = "username_index", unique = true)
    private String username;

    @NotBlank(message = "Email of AppUser cannot be blank")
    @Size(max = 40,message = "Maximum size of email is 40")
    @Email(message = "Email of AppUser is not valid")
    @Indexed(name = "email_index", unique = true)
    private String email;
    @NotBlank(message = "Password of AppUser cannot be blank")
    private String password;

    private AccessToken accessToken;

    private RefreshToken refreshToken;

    @CreatedDate
    private Instant createdAt;

    @NotNull(message = "Roles of AppUser cannot be null")
    @DBRef(lazy = true)
    private Set<Role> roles;

    @Version
    private Long version;

    public AppUser(){
        this.roles = new HashSet<>();
    }

    public AppUser(@NonNull String username, @NonNull String email, @NonNull String password) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,email);
    }

    @Override
    public boolean equals(Object user) {
        if (user == this) return true;

        if (user == null || user.getClass() != this.getClass()) return false;

        AppUser guest = (AppUser) user;
        return this.getId().equals(guest.getId());
    }
}
