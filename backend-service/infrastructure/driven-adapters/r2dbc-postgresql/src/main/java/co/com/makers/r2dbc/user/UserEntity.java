package co.com.makers.r2dbc.user;

import co.com.makers.model.user.valueobject.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Table("users")
public class UserEntity {

    @Id
    private UUID id;

    @Column("role")
    private Role role;

    @Column("login_identifier")
    private String loginIdentifier;

    @Column("password")
    private String password;

    @Column("active")
    private Boolean active;

    @CreatedDate
    @Column("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private Instant updatedAt;

    public UserEntity() {
    }

    public UserEntity(UUID id, Role role, String loginIdentifier, String password, Boolean active) {
        this.id = id;
        this.role = role;
        this.loginIdentifier = loginIdentifier;
        this.password = password;
        this.active = active;
    }
}