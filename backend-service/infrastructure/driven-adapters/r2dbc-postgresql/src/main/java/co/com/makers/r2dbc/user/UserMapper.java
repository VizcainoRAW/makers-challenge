package co.com.makers.r2dbc.user;

import co.com.makers.model.user.User;
import co.com.makers.model.user.valueobject.LoginIdentifier;

public class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return new User(
                entity.getId(),
                entity.getRole(),
                new LoginIdentifier(entity.getLoginIdentifier()),
                entity.getPassword(),
                entity.getActive()
        );
    }

    public static UserEntity toEntity(User domain) {
        if (domain == null) return null;
        return new UserEntity(
                domain.id(),
                domain.role(),
                domain.loginIdentifier() != null ? domain.loginIdentifier().getValue() : null,
                domain.password(),
                domain.active()
        );
    }
}