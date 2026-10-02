package co.com.makers.r2dbc.user;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserReactiveRepository extends ReactiveCrudRepository<Object, String>,
        ReactiveQueryByExampleExecutor<Object> {}
