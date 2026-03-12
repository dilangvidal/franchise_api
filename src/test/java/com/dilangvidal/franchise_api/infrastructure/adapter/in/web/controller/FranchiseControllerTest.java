package com.dilangvidal.franchise_api.infrastructure.adapter.in.web.controller;

import com.dilangvidal.franchise_api.application.usecase.FranchiseUseCase;
import com.dilangvidal.franchise_api.domain.exception.BranchNotFoundException;
import com.dilangvidal.franchise_api.domain.exception.DuplicateNameException;
import com.dilangvidal.franchise_api.domain.exception.FranchiseNotFoundException;
import com.dilangvidal.franchise_api.domain.exception.ProductNotFoundException;
import com.dilangvidal.franchise_api.domain.model.Branch;
import com.dilangvidal.franchise_api.domain.model.Franchise;
import com.dilangvidal.franchise_api.domain.model.Product;
import com.dilangvidal.franchise_api.domain.model.TopStockProduct;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.request.BranchRequest;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.request.FranchiseRequest;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.request.ProductRequest;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.request.StockUpdateRequest;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.handler.GlobalExceptionHandler;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.mapper.FranchiseWebMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para el FranchiseController (capa web / endpoints HTTP).
 *
 * Estrategia:
 * - @WebFluxTest carga SOLO el controlador indicado, sin levantar toda la app ni la BD.
 * - @MockitoBean reemplaza el FranchiseUseCase real por un mock dentro del contexto de Spring.
 * - @Import incluye FranchiseWebMapper (para convertir dominio a respuesta)
 *   y GlobalExceptionHandler (para probar los codigos HTTP de error).
 *
 * Validaciones realizadas por cada endpoint:
 * 1. Codigo de estado HTTP (200, 201, 400, 404, 409)
 * 2. Tiempo de respuesta (menor a 500ms)
 * 3. Headers HTTP (Content-Type)
 * 4. Estructura del JSON (campos existentes)
 * 5. Contenido del JSON (valores esperados)
 * 6. Tamano de listas cuando aplica
 * 7. Validacion de request body
 * 8. Validacion de errores (inputs invalidos)
 * 9. Recurso no existente (404)
 * 10. Verificacion de interaccion con el servicio (Mockito verify)
 * 11. Casos borde (lista vacia, nombre vacio, id inexistente, stock negativo)
 */
@WebFluxTest(FranchiseController.class)
@Import({ FranchiseWebMapper.class, GlobalExceptionHandler.class })
class FranchiseControllerTest {

    // Cliente HTTP de prueba que Spring inyecta automaticamente con @WebFluxTest
    @Autowired
    private WebTestClient webTestClient;

    // Mock del caso de uso: se simula la logica de negocio
    @MockitoBean
    private FranchiseUseCase franchiseUseCase;

    private static final String BASE_URL = "/api/v1/franchises";

    // Auxiliar para crear franquicia de ejemplo
    private Franchise sampleFranchise() {
        Product p1 = Product.builder().id("prod-1").name("Pollo Frisby").stock(100).build();
        Product p2 = Product.builder().id("prod-2").name("Hamburguesa").stock(250).build();
        Branch b1 = Branch.builder().id("branch-1").name("Sucursal Norte")
                .products(new ArrayList<>(List.of(p1, p2))).build();
        return Franchise.builder().id("franc-1").name("Frisby")
                .branches(new ArrayList<>(List.of(b1))).build();
    }
}
