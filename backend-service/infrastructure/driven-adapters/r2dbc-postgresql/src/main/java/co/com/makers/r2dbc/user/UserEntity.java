package co.com.makers.r2dbc.user;

import co.com.makers.model.user.valueobject.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Table("users")
public class UserEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column("role")
    private String role;

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

    @Transient
    private boolean isNew = false;

    public UserEntity() {
    }

    public UserEntity(UUID id, Role role, String loginIdentifier, String password, Boolean active) {
        this.id = id;
        this.role = role != null ? role.name() : null;
        this.loginIdentifier = loginIdentifier;
        this.password = password;
        this.active = active;
    }

    @Override
    public boolean isNew() {
        return this.isNew || this.id == null;
    }
}