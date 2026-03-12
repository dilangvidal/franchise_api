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

}