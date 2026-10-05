package co.com.makers.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoanApplicationRouterRest {
    @Bean
    public RouterFunction<ServerResponse> loanApplicationRouterFunction(LoanApplicationHandler handler) {
        return route(POST("/api/loans"), handler::createLoanApplication)
                .andRoute(GET("/api/admin/loans"), handler::getAllLoanApplications)
                .andRoute(PATCH("/api/admin/loans/{id}/status"), handler::updateLoanStatus);
    }
}
