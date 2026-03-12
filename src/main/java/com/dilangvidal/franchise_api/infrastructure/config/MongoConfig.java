package com.dilangvidal.franchise_api.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * @Configuration: Esta anotación indica que la clase proporciona configuraciones para el contector de Spring.
 * @EnableReactiveMongoRepositories: Habilita el escaneo de interfaces hijas de ReactiveMongoRepository
 * en el paquete especificado para instanciarlas y proveer su implementación usando datos reactivos de Spring.
 */
@Configuration
@EnableReactiveMongoRepositories(
        basePackages = "com.dilangvidal.franchise_api.infrastructure.adapter.out.persistence.repository"
)
public class MongoConfig {

}