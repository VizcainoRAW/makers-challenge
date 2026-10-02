package co.com.makers.model.user.gateways;

import co.com.makers.model.user.User;

public interface UserRepository {
    void save(User user);
}
