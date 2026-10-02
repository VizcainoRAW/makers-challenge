package co.com.makers.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterRest {
    @Bean
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler handler) {
        return route(POST("/api/users"), handler::createUser)
                .andRoute(POST("/api/auth/login"), handler::login)
                .andRoute(POST("/api/users/login"), handler::login);
    }
}
