package co.com.makers.r2dbc.user;

import co.com.makers.model.user.User;
import co.com.makers.model.user.valueobject.LoginIdentifier;
import co.com.makers.model.user.valueobject.Role;

public class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        Role role = entity.getRole() != null ? Role.valueOf(entity.getRole()) : null;
        return new User(
                entity.getId(),
                role,
                entity.getLoginIdentifier() != null ? new LoginIdentifier(entity.getLoginIdentifier()) : null,
                entity.getPassword(),
                entity.getActive()
        );
    }

    public static UserEntity toEntity(User domain) {
        if (domain == null) return null;
        UserEntity entity = new UserEntity(
                domain.id(),
                domain.role(),
                domain.loginIdentifier() != null ? domain.loginIdentifier().getValue() : null,
                domain.password(),
                domain.active()
        );
        entity.setNew(true);
        return entity;
    }
}