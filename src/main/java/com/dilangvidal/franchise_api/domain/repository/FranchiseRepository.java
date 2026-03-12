package com.dilangvidal.franchise_api.domain.repository;

import com.dilangvidal.franchise_api.domain.model.Franchise;
import com.dilangvidal.franchise_api.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Interfaz de repositorio del Dominio para Franchise.
 * Utiliza clases Mono y Flux de Project Reactor para programación reactiva (no bloqueante).
 * Mono<T>: Representa un flujo que emite 0 o 1 elemento de tipo T. 
 * Flux<T>: Representa un flujo que emite 0 a N elementos de tipo T.
 */
public interface FranchiseRepository {

    Mono<Franchise> save(Franchise franchise);

    Mono<Franchise> findById(String id);

    Mono<Boolean> existsByName(String name);

    Flux<Franchise> findAll();

    Mono<Franchise> addBranch(String franchiseId, String branchName);

    Mono<Franchise> addProduct(String franchiseId, String branchId, String productName, Integer stock);

    Mono<Franchise> deleteProduct(String franchiseId, String branchId, String productId);

    Mono<Franchise> updateStock(String franchiseId, String branchId, String productId, Integer newStock);

    Mono<Franchise> updateFranchiseName(String franchiseId, String newName);

    Mono<Franchise> updateBranchName(String franchiseId, String branchId, String newName);

    Mono<Franchise> updateProductName(String franchiseId, String branchId, String productId, String newName);

    Mono<List<Product>> getTopStockProductPerBranch(String franchiseId);
}