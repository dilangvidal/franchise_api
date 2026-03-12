package com.dilangvidal.franchise_api.infrastructure.adapter.in.web.controller;

import com.dilangvidal.franchise_api.application.usecase.FranchiseUseCase;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.request.BranchRequest;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.request.FranchiseRequest;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.request.ProductRequest;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.request.StockUpdateRequest;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.response.FranchiseResponse;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.response.TopStockProductResponse;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.mapper.FranchiseWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/franchises")
@RequiredArgsConstructor
@Tag(name = "Franquicias", description = "Endpoints para la gestión de franquicias, sucursales y productos")
public class FranchiseController {

        private final FranchiseUseCase franchiseUseCase;
        private final FranchiseWebMapper webMapper;

}