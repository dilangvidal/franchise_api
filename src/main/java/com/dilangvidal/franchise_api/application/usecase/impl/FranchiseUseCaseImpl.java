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
}