package com.dilangvidal.franchise_api.infrastructure.adapter.out.persistence.repository;
import com.dilangvidal.franchise_api.infrastructure.adapter.out.persistence.document.FranchiseDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @Repository: Indica a Spring que esta interfaz es un repositorio de base de datos.
 * ReactiveMongoRepository: Interfaz de Spring Data que proporciona métodos CRUD no bloqueantes,
 * devolviendo siempre tipos de Reactor (Mono o Flux) en lugar de entidades planas.
 */
@Repository
public interface FranchiseReactiveRepository
        extends ReactiveMongoRepository<FranchiseDocument, String> {

    Mono<FranchiseDocument> findByName(String name);

    Mono<Boolean> existsByName(String name);
}