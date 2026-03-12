package com.dilangvidal.franchise_api.application.usecase.impl;

import com.dilangvidal.franchise_api.application.usecase.FranchiseUseCase;
import com.dilangvidal.franchise_api.domain.exception.DuplicateNameException;
import com.dilangvidal.franchise_api.domain.exception.FranchiseNotFoundException;
import com.dilangvidal.franchise_api.domain.model.Franchise;
import com.dilangvidal.franchise_api.domain.model.TopStockProduct;
import com.dilangvidal.franchise_api.domain.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FranchiseUseCaseImpl implements FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Franchise> createFranchise(String name) {
        log.info("Creando franquicia con nombre: {}", name);
        return franchiseRepository.existsByName(name)
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new DuplicateNameException("Franchise", name));
                    }
                    Franchise franchise = Franchise.builder()
                            .name(name)
                            .build();
                    return franchiseRepository.save(franchise);
                });
    }
    @Override
    public Flux<Franchise> getAllFranchises() {
        log.info("Obtener todas las franquicias");
        return franchiseRepository.findAll();
    }
    @Override
    public Mono<Franchise> updateFranchiseName(String franchiseId, String newName) {
        log.info("Actualizando nombre de la franquicia: {} → {}", franchiseId, newName);
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> franchiseRepository.existsByName(newName))
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new DuplicateNameException("Franchise", newName));
                    }
                    return franchiseRepository.updateFranchiseName(franchiseId, newName);
                });
    }
    @Override
    public Mono<Franchise> addBranch(String franchiseId, String branchName) {
        log.info("Agregando sucursal '{}' a franquicia: {}", branchName, franchiseId);
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    boolean branchExists = franchise.getBranches().stream()
                            .anyMatch(b -> b.getName().equalsIgnoreCase(branchName));
                    if (branchExists) {
                        return Mono.error(new DuplicateNameException("Branch", branchName));
                    }
                    return franchiseRepository.addBranch(franchiseId, branchName);
                });
    }
    @Override
    public Mono<Franchise> updateBranchName(String franchiseId, String branchId, String newName) {
        log.info("Actualizando nombre de la sucursal: {} → {}", branchId, newName);
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> franchiseRepository.updateBranchName(
                        franchiseId, branchId, newName));
    }
    @Override
    public Mono<Franchise> addProduct(String franchiseId, String branchId,
                                      String productName, Integer stock) {
        log.info("Agregando producto '{}' a sucursal: {} en franquicia: {}",
                productName, branchId, franchiseId);
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    boolean branchExists = franchise.getBranches().stream()
                            .anyMatch(b -> b.getId().equals(branchId));
                    if (!branchExists) {
                        return Mono.error(new com.dilangvidal.franchise_api
                                .domain.exception.BranchNotFoundException(branchId));
                    }
                    return franchiseRepository.addProduct(franchiseId, branchId, productName, stock);
                });
    }
    @Override
    public Mono<Franchise> deleteProduct(String franchiseId, String branchId, String productId) {
        log.info("Eliminando producto: {} de sucursal: {} en franquicia: {}",
                productId, branchId, franchiseId);
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    boolean productExists = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .flatMap(b -> b.getProducts().stream())
                            .anyMatch(p -> p.getId().equals(productId));
                    if (!productExists) {
                        return Mono.error(new com.dilangvidal.franchise_api
                                .domain.exception.ProductNotFoundException(productId));
                    }
                    return franchiseRepository.deleteProduct(franchiseId, branchId, productId);
                });
    }
    @Override
    public Mono<Franchise> updateStock(String franchiseId, String branchId,
                                       String productId, Integer newStock) {
        log.info("Actualizando stock de producto: {} a {} en sucursal: {} franquicia: {}",
                productId, newStock, branchId, franchiseId);
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> franchiseRepository.updateStock(
                        franchiseId, branchId, productId, newStock));
    }
}