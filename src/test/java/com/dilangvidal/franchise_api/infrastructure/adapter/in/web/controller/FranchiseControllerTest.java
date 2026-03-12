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

    // POST /api/v1/franchises
    @Nested
    @DisplayName("POST /api/v1/franchises")
    class CreateFranchise {

        @Test
        @DisplayName("Debe crear franquicia con nombre valido y retornar 201")
        void shouldReturn201WhenCreated() throws InterruptedException {
            Thread.sleep(110);
            long start = System.currentTimeMillis();
            Franchise franchise = Franchise.builder()
                    .id("franc-1").name("Frisby").branches(new ArrayList<>()).build();

            when(franchiseUseCase.createFranchise("Frisby")).thenReturn(Mono.just(franchise));

            webTestClient.post().uri(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new FranchiseRequest("Frisby"))
                    .exchange()
                    .expectStatus().isCreated()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.id").exists()
                    .jsonPath("$.id").isEqualTo("franc-1")
                    .jsonPath("$.name").exists()
                    .jsonPath("$.name").isEqualTo("Frisby")
                    .jsonPath("$.branches").exists()
                    .jsonPath("$.branches").isArray();

            long duration = System.currentTimeMillis() - start;
            assertTrue(duration < 500, "La respuesta tardo demasiado");
            verify(franchiseUseCase).createFranchise("Frisby");
        }

        @Test
        @DisplayName("Debe retornar 409 cuando el nombre ya existe")
        void shouldReturn409WhenDuplicateName() throws InterruptedException {
            Thread.sleep(110);
            long start = System.currentTimeMillis();
            when(franchiseUseCase.createFranchise("Frisby"))
                    .thenReturn(Mono.error(new DuplicateNameException("Franchise", "Frisby")));

            webTestClient.post().uri(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new FranchiseRequest("Frisby"))
                    .exchange()
                    .expectStatus().isEqualTo(409)
                    .expectBody()
                    .jsonPath("$.status").isEqualTo(409);

            long duration = System.currentTimeMillis() - start;
            assertTrue(duration < 500, "La respuesta tardo demasiado");
            verify(franchiseUseCase).createFranchise("Frisby");
        }

        @Test
        @DisplayName("Debe rechazar con 400 cuando el nombre esta en blanco")
        void shouldFailWhenNameIsEmpty() throws InterruptedException {
            Thread.sleep(110);
            long start = System.currentTimeMillis();
            webTestClient.post().uri(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new FranchiseRequest(""))
                    .exchange()
                    .expectStatus().isBadRequest();

            long duration = System.currentTimeMillis() - start;
            assertTrue(duration < 500, "La respuesta tardo demasiado");
            verify(franchiseUseCase, never()).createFranchise(any());
        }

        @Test
        @DisplayName("Debe rechazar con 400 cuando el nombre es muy corto")
        void shouldFailWhenPayloadInvalid() throws InterruptedException {
            Thread.sleep(110);
            long start = System.currentTimeMillis();
            webTestClient.post().uri(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new FranchiseRequest("A"))
                    .exchange()
                    .expectStatus().isBadRequest();

            long duration = System.currentTimeMillis() - start;
            assertTrue(duration < 500, "La respuesta tardo demasiado");
            verify(franchiseUseCase, never()).createFranchise(any());
        }
    }

    // GET /api/v1/franchises
    @Nested
    @DisplayName("GET /api/v1/franchises")
    class GetAllFranchises {

        @Test
        @DisplayName("Debe retornar lista de franquicias con 200")
        void shouldReturnFranchises() throws InterruptedException {
            Thread.sleep(110);
            long start = System.currentTimeMillis();
            Franchise f1 = Franchise.builder().id("franc-1").name("Frisby").branches(new ArrayList<>()).build();
            Franchise f2 = Franchise.builder().id("franc-2").name("KFC").branches(new ArrayList<>()).build();

            when(franchiseUseCase.getAllFranchises()).thenReturn(Flux.just(f1, f2));

            webTestClient.get().uri(BASE_URL)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.length()").isEqualTo(2)
                    .jsonPath("$[0].name").isEqualTo("Frisby")
                    .jsonPath("$[1].name").isEqualTo("KFC");

            long duration = System.currentTimeMillis() - start;
            assertTrue(duration < 500, "La respuesta tardo demasiado");
            verify(franchiseUseCase).getAllFranchises();
        }

        @Test
        @DisplayName("Debe retornar lista vacia cuando no hay franquicias")
        void shouldReturnEmptyList() throws InterruptedException {
            Thread.sleep(110);
            long start = System.currentTimeMillis();
            when(franchiseUseCase.getAllFranchises()).thenReturn(Flux.empty());

            webTestClient.get().uri(BASE_URL)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.length()").isEqualTo(0)
                    .jsonPath("$").isArray();

            long duration = System.currentTimeMillis() - start;
            assertTrue(duration < 500, "La respuesta tardo demasiado");
            verify(franchiseUseCase).getAllFranchises();
        }
    }

    // PATCH /{franchiseId}/name
    @Nested
    @DisplayName("PATCH /{franchiseId}/name")
    class UpdateFranchiseName {

        @Test
        @DisplayName("Debe actualizar el nombre de la franquicia con 200")
        void shouldReturn200WhenUpdated() throws InterruptedException {
            Thread.sleep(110);
            long start = System.currentTimeMillis();
            Franchise updated = Franchise.builder()
                    .id("franc-1").name("Frisby Premium").branches(new ArrayList<>()).build();

            when(franchiseUseCase.updateFranchiseName("franc-1", "Frisby Premium"))
                    .thenReturn(Mono.just(updated));

            webTestClient.patch().uri(BASE_URL + "/franc-1/name")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new FranchiseRequest("Frisby Premium"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.id").exists()
                    .jsonPath("$.name").isEqualTo("Frisby Premium");

            long duration = System.currentTimeMillis() - start;
            assertTrue(duration < 500, "La respuesta tardo demasiado");
            verify(franchiseUseCase).updateFranchiseName("franc-1", "Frisby Premium");
        }

        @Test
        @DisplayName("Debe retornar 404 cuando la franquicia no existe")
        void shouldReturn404WhenNotFound() throws InterruptedException {
            Thread.sleep(110);
            long start = System.currentTimeMillis();
            when(franchiseUseCase.updateFranchiseName(eq("no-existe"), anyString()))
                    .thenReturn(Mono.error(new FranchiseNotFoundException("no-existe")));

            webTestClient.patch().uri(BASE_URL + "/no-existe/name")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new FranchiseRequest("Nuevo Nombre"))
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody()
                    .jsonPath("$.status").isEqualTo(404);

            long duration = System.currentTimeMillis() - start;
            assertTrue(duration < 500, "La respuesta tardo demasiado");
        }

        @Test
        @DisplayName("Debe fallar con 400 si el payload es invalido")
        void shouldFailWhenPayloadInvalid() throws InterruptedException {
            Thread.sleep(110);
            long start = System.currentTimeMillis();
            webTestClient.patch().uri(BASE_URL + "/franc-1/name")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new FranchiseRequest(""))
                    .exchange()
                    .expectStatus().isBadRequest();

            long duration = System.currentTimeMillis() - start;
            assertTrue(duration < 500, "La respuesta tardo demasiado");
            verify(franchiseUseCase, never()).updateFranchiseName(any(), any());
        }
    }

    // POST /{franchiseId}/branches
    @Nested
    @DisplayName("POST /{franchiseId}/branches")
    class AddBranch {

        @Test
        @DisplayName("Debe agregar sucursal exitosamente con 201")
        void shouldCreateBranch() throws InterruptedException {
            Thread.sleep(110);
            long start = System.currentTimeMillis();
            Franchise updated = sampleFranchise();

            when(franchiseUseCase.addBranch("franc-1", "Sucursal Norte"))
                    .thenReturn(Mono.just(updated));

            webTestClient.post().uri(BASE_URL + "/franc-1/branches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new BranchRequest("Sucursal Norte"))
                    .exchange()
                    .expectStatus().isCreated()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.branches[0].name").isEqualTo("Sucursal Norte");

            long duration = System.currentTimeMillis() - start;
            assertTrue(duration < 500, "La respuesta tardo demasiado");
            verify(franchiseUseCase).addBranch("franc-1", "Sucursal Norte");
        }

        @Test
        @DisplayName("Debe retornar 404 cuando la franquicia no existe")
        void shouldReturn404WhenFranchiseNotFound() throws InterruptedException {
            Thread.sleep(110);
            long start = System.currentTimeMillis();
            when(franchiseUseCase.addBranch(eq("no-existe"), anyString()))
                    .thenReturn(Mono.error(new FranchiseNotFoundException("no-existe")));

            webTestClient.post().uri(BASE_URL + "/no-existe/branches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new BranchRequest("Sucursal"))
                    .exchange()
                    .expectStatus().isNotFound();

            long duration = System.currentTimeMillis() - start;
            assertTrue(duration < 500, "La respuesta tardo demasiado");
        }

        @Test
        @DisplayName("Debe rechazar con 400 cuando el nombre esta en blanco")
        void shouldFailWhenNameIsEmpty() throws InterruptedException {
            Thread.sleep(110);
            long start = System.currentTimeMillis();
            webTestClient.post().uri(BASE_URL + "/franc-1/branches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new BranchRequest(""))
                    .exchange()
                    .expectStatus().isBadRequest();

            long duration = System.currentTimeMillis() - start;
            assertTrue(duration < 500, "La respuesta tardo demasiado");
            verify(franchiseUseCase, never()).addBranch(any(), any());
        }
    }
}
