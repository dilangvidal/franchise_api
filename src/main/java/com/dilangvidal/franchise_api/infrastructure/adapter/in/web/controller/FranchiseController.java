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

        @Operation(summary = "Crear una nueva franquicia")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Franquicia creada exitosamente", content = @Content(schema = @Schema(implementation = FranchiseResponse.class))),
                        @ApiResponse(responseCode = "409", description = "El nombre de la franquicia ya existe", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "400", description = "Cuerpo de solicitud inválido", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<FranchiseResponse> createFranchise(
                        @Valid @RequestBody FranchiseRequest request) {
                return franchiseUseCase.createFranchise(request.name())
                                .map(webMapper::toResponse);
        }
        @Operation(summary = "Obtener todas las franquicias")
        @ApiResponse(responseCode = "200", description = "Lista de todas las franquicias")
        @GetMapping
        public Flux<FranchiseResponse> getAllFranchises() {
                return franchiseUseCase.getAllFranchises()
                                .map(webMapper::toResponse);
        }
        @Operation(summary = "Actualizar el nombre de una franquicia")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Nombre de la franquicia actualizado"),
                        @ApiResponse(responseCode = "404", description = "Franquicia no encontrada", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "409", description = "El nombre ya está en uso", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PatchMapping("/{franchiseId}/name")
        public Mono<FranchiseResponse> updateFranchiseName(
                        @Parameter(description = "ID de la franquicia", example = "64b1f2e3c2a4e12d3f456789") @PathVariable String franchiseId,
                        @Valid @RequestBody FranchiseRequest request) {
                return franchiseUseCase.updateFranchiseName(franchiseId, request.name())
                                .map(webMapper::toResponse);
        }
        @Operation(summary = "Agregar una sucursal a una franquicia")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Sucursal agregada exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Franquicia no encontrada", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "409", description = "El nombre de la sucursal ya existe", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PostMapping("/{franchiseId}/branches")
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<FranchiseResponse> addBranch(
                        @Parameter(description = "ID de la franquicia", example = "64b1f2e3c2a4e12d3f456789") @PathVariable String franchiseId,
                        @Valid @RequestBody BranchRequest request) {
                return franchiseUseCase.addBranch(franchiseId, request.name())
                                .map(webMapper::toResponse);
        }
        @Operation(summary = "Actualizar el nombre de una sucursal")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Nombre de la sucursal actualizado"),
                        @ApiResponse(responseCode = "404", description = "Franquicia o sucursal no encontrada", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PatchMapping("/{franchiseId}/branches/{branchId}/name")
        public Mono<FranchiseResponse> updateBranchName(
                        @Parameter(description = "ID de la franquicia") @PathVariable String franchiseId,
                        @Parameter(description = "ID de la sucursal") @PathVariable String branchId,
                        @Valid @RequestBody BranchRequest request) {
                return franchiseUseCase.updateBranchName(franchiseId, branchId, request.name())
                                .map(webMapper::toResponse);
        }
}