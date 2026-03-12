package com.dilangvidal.franchise_api.infrastructure.adapter.out.persistence.adapter;


import com.dilangvidal.franchise_api.domain.exception.FranchiseNotFoundException;
import com.dilangvidal.franchise_api.domain.model.Branch;
import com.dilangvidal.franchise_api.domain.model.Franchise;
import com.dilangvidal.franchise_api.domain.model.Product;
import com.dilangvidal.franchise_api.domain.model.TopStockProduct;
import com.dilangvidal.franchise_api.domain.repository.FranchiseRepository;
import com.dilangvidal.franchise_api.infrastructure.adapter.out.persistence.document.BranchDocument;
import com.dilangvidal.franchise_api.infrastructure.adapter.out.persistence.document.ProductDocument;
import com.dilangvidal.franchise_api.infrastructure.adapter.out.persistence.mapper.FranchiseDocumentMapper;
import com.dilangvidal.franchise_api.infrastructure.adapter.out.persistence.repository.FranchiseReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * @Component: Anotación de Spring que registra esta clase automáticamente para inyección de dependencias.
 * @RequiredArgsConstructor: Anotación de Lombok que genera automáticamente un constructor
 * donde se inyectan las variables marcadas como 'final' (como el repository y el mapper).
 */
@Component
@RequiredArgsConstructor
public class FranchisePersistenceAdapter implements FranchiseRepository {

    private final FranchiseReactiveRepository repository;
    private final FranchiseDocumentMapper mapper;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return repository.save(mapper.toDocument(franchise))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Flux<Franchise> findAll() {
        return repository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> addBranch(String franchiseId, String branchName) {
        return repository.findById(franchiseId)
                .flatMap(doc -> {
                    BranchDocument newBranch = BranchDocument.builder()
                            .id(UUID.randomUUID().toString())
                            .name(branchName)
                            .products(new ArrayList<>())
                            .build();
                    doc.getBranches().add(newBranch);
                    return repository.save(doc);
                })
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> addProduct(String franchiseId, String branchId,
                                      String productName, Integer stock) {
        return repository.findById(franchiseId)
                .flatMap(doc -> {
                    doc.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .ifPresent(branch -> {
                                ProductDocument newProduct = ProductDocument.builder()
                                        .id(UUID.randomUUID().toString())
                                        .name(productName)
                                        .stock(stock)
                                        .build();
                                branch.getProducts().add(newProduct);
                            });
                    return repository.save(doc);
                })
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> deleteProduct(String franchiseId, String branchId, String productId) {
        return repository.findById(franchiseId)
                .flatMap(doc -> {
                    doc.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .ifPresent(branch ->
                                    branch.getProducts().removeIf(p -> p.getId().equals(productId))
                            );
                    return repository.save(doc);
                })
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> updateStock(String franchiseId, String branchId,
                                       String productId, Integer newStock) {
        return repository.findById(franchiseId)
                // flatMap transforma cada elemento emitido por el flujo original de forma asíncrona.
                // Aquí extraemos un documento Franchise y navegamos un stream sincrónico de sucursales adentro.
                .flatMap(doc -> {
                    doc.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .flatMap(branch -> branch.getProducts().stream()
                                    .filter(p -> p.getId().equals(productId))
                                    .findFirst())
                            .ifPresent(product -> product.setStock(newStock));
                    return repository.save(doc);
                })
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> updateFranchiseName(String franchiseId, String newName) {
        return repository.findById(franchiseId)
                .flatMap(doc -> {
                    doc.setName(newName);
                    return repository.save(doc);
                })
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> updateBranchName(String franchiseId, String branchId, String newName) {
        return repository.findById(franchiseId)
                .flatMap(doc -> {
                    doc.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .ifPresent(branch -> branch.setName(newName));
                    return repository.save(doc);
                })
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> updateProductName(String franchiseId, String branchId,
                                             String productId, String newName) {
        return repository.findById(franchiseId)
                .flatMap(doc -> {
                    doc.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .flatMap(branch -> branch.getProducts().stream()
                                    .filter(p -> p.getId().equals(productId))
                                    .findFirst())
                            .ifPresent(product -> product.setName(newName));
                    return repository.save(doc);
                })
                .map(mapper::toDomain);
    }

    @Override
    public Mono<List<TopStockProduct>> getTopStockProductPerBranch(String franchiseId) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .map(doc -> doc.getBranches().stream()
                        .flatMap(branch -> branch.getProducts().stream()
                                .max(Comparator.comparingInt(ProductDocument::getStock))
                                .map(topProduct -> TopStockProduct.builder()
                                        .branchId(branch.getId())
                                        .branchName(branch.getName())
                                        .productId(topProduct.getId())
                                        .productName(topProduct.getName())
                                        .stock(topProduct.getStock())
                                        .build())
                                .stream())
                        .toList());
    }
}