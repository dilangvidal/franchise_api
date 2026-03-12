package com.dilangvidal.franchise_api.application.usecase;

import com.dilangvidal.franchise_api.domain.model.Franchise;
import com.dilangvidal.franchise_api.domain.model.TopStockProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FranchiseUseCase {

    Mono<Franchise> createFranchise(String name);
    Flux<Franchise> getAllFranchises();
    Mono<Franchise> updateFranchiseName(String franchiseId, String newName);
    Mono<Franchise> addBranch(String franchiseId, String branchName);
    Mono<Franchise> updateBranchName(String franchiseId, String branchId, String newName);
}