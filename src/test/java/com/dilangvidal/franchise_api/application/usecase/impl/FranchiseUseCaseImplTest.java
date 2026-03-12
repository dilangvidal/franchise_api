package com.dilangvidal.franchise_api.application.usecase.impl;

import com.dilangvidal.franchise_api.domain.exception.BranchNotFoundException;
import com.dilangvidal.franchise_api.domain.exception.DuplicateNameException;
import com.dilangvidal.franchise_api.domain.exception.FranchiseNotFoundException;
import com.dilangvidal.franchise_api.domain.exception.ProductNotFoundException;
import com.dilangvidal.franchise_api.domain.model.Branch;
import com.dilangvidal.franchise_api.domain.model.Franchise;
import com.dilangvidal.franchise_api.domain.model.Product;
import com.dilangvidal.franchise_api.domain.model.TopStockProduct;
import com.dilangvidal.franchise_api.domain.repository.FranchiseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para FranchiseUseCaseImpl (capa de lógica de negocio).
 *
 * Estrategia:
 * - Se utiliza @Mock para simular el FranchiseRepository, de modo que la prueba
 * se enfoque exclusivamente en la lógica del caso de uso sin depender de la BD.
 * - Se utiliza @InjectMocks para inyectar automáticamente el mock dentro del
 * UseCase.
 * - Se utiliza StepVerifier de Project Reactor para verificar los flujos
 * reactivos (Mono/Flux),
 * ya que no podemos usar assertEquals directamente con tipos reactivos.
 *
 * Ejemplo de flujo:
 * when(repository.metodo()).thenReturn(Mono.just(valor)); -> Configura qué
 * devuelve el mock
 * StepVerifier.create(useCase.metodo()) -> Ejecuta el método reactivo
 * .expectNextMatches(resultado -> condición) -> Verifica el resultado emitido
 * .verifyComplete(); -> Verifica que el flujo terminó correctamente
 */
@ExtendWith(MockitoExtension.class) // Habilita las anotaciones de Mockito (@Mock, @InjectMocks)
class FranchiseUseCaseImplTest {

        // Mock del repositorio: simula las operaciones de base de datos
        @Mock
        private FranchiseRepository franchiseRepository;

        // Instancia real del caso de uso, con el repositorio mockeado inyectado
        @InjectMocks
        private FranchiseUseCaseImpl franchiseUseCase;

        // ─── Métodos auxiliares para construir datos de prueba ────────────────────

        private Product buildProduct(String id, String name, int stock) {
                return Product.builder().id(id).name(name).stock(stock).build();
        }

        private Branch buildBranch(String id, String name, List<Product> products) {
                return Branch.builder().id(id).name(name)
                                .products(new ArrayList<>(products)).build();
        }

        private Franchise buildFranchise(String id, String name, List<Branch> branches) {
                return Franchise.builder().id(id).name(name)
                                .branches(new ArrayList<>(branches)).build();
        }

        /**
         * Crea una franquicia de ejemplo con:
         * - 1 sucursal ("Sucursal Norte")
         * - 2 productos ("Pollo Frisby" stock=100, "Hamburguesa" stock=250)
         */
        private Franchise sampleFranchise() {
                Product p1 = buildProduct("prod-1", "Pollo Frisby", 100);
                Product p2 = buildProduct("prod-2", "Hamburguesa", 250);
                Branch b1 = buildBranch("branch-1", "Sucursal Norte",
                                new ArrayList<>(List.of(p1, p2)));
                return buildFranchise("franc-1", "Frisby", new ArrayList<>(List.of(b1)));
        }

        @Nested
        @DisplayName("createFranchise")
        class CreateFranchise {

                @Test
                @DisplayName("Debe crear franquicia cuando el nombre no existe")
                void shouldCreateFranchiseWhenNameDoesNotExist() {
                        // Datos de prueba: la franquicia que el repositorio "devolvería" al guardar
                        Franchise saved = buildFranchise("franc-1", "Frisby", new ArrayList<>());

                        // Configurar mocks: el nombre NO existe -> se procede a guardar
                        when(franchiseRepository.existsByName("Frisby")).thenReturn(Mono.just(false));
                        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(saved));

                        // Ejecutar y verificar: debe emitir la franquicia creada y completar
                        StepVerifier.create(franchiseUseCase.createFranchise("Frisby"))
                                        .expectNextMatches(f -> f.getId().equals("franc-1")
                                                        && f.getName().equals("Frisby"))
                                        .verifyComplete();

                        // Verificar que se llamaron los métodos esperados del repositorio
                        verify(franchiseRepository).existsByName("Frisby");
                        verify(franchiseRepository).save(any(Franchise.class));
                }

        @Test
        @DisplayName("Debe lanzar DuplicateNameException cuando el nombre ya existe")
        void shouldThrowDuplicateWhenNameExists() {
            // Configurar mock: el nombre YA existe
            when(franchiseRepository.existsByName("Frisby")).thenReturn(Mono.just(true));

            // Ejecutar y verificar: debe emitir un error DuplicateNameException
            StepVerifier.create(franchiseUseCase.createFranchise("Frisby"))
                    .expectError(DuplicateNameException.class)
                    .verify();

            // Verificar que NUNCA se intentó guardar (porque el nombre ya existía)
            verify(franchiseRepository, never()).save(any());
        }
        }
        @Nested
        @DisplayName("getAllFranchises")
        class GetAllFranchises {

                @Test
                @DisplayName("Debe retornar todas las franquicias")
                void shouldReturnAllFranchises() {
                        Franchise f1 = buildFranchise("franc-1", "Frisby", new ArrayList<>());
                        Franchise f2 = buildFranchise("franc-2", "KFC", new ArrayList<>());

                        when(franchiseRepository.findAll()).thenReturn(Flux.just(f1, f2));

                        // StepVerifier verifica cada elemento emitido por el Flux en orden
                        StepVerifier.create(franchiseUseCase.getAllFranchises())
                                        .expectNextMatches(f -> f.getName().equals("Frisby"))
                                        .expectNextMatches(f -> f.getName().equals("KFC"))
                                        .verifyComplete();
                }

        @Test
        @DisplayName("Debe retornar flujo vacío cuando no hay franquicias")
        void shouldReturnEmptyWhenNoFranchises() {
            when(franchiseRepository.findAll()).thenReturn(Flux.empty());

            // verifyComplete() confirma que el flujo terminó sin emitir elementos
            StepVerifier.create(franchiseUseCase.getAllFranchises())
                    .verifyComplete();
        }
        }
        @Nested
        @DisplayName("updateFranchiseName")
        class UpdateFranchiseName {

                @Test
                @DisplayName("Debe actualizar nombre de franquicia exitosamente")
                void shouldUpdateNameSuccessfully() {
                        Franchise franchise = sampleFranchise();
                        Franchise updated = buildFranchise("franc-1", "Frisby Premium",
                                        franchise.getBranches());

                        when(franchiseRepository.findById("franc-1")).thenReturn(Mono.just(franchise));
                        when(franchiseRepository.existsByName("Frisby Premium"))
                                        .thenReturn(Mono.just(false));
                        when(franchiseRepository.updateFranchiseName("franc-1", "Frisby Premium"))
                                        .thenReturn(Mono.just(updated));

                        StepVerifier.create(franchiseUseCase.updateFranchiseName(
                                        "franc-1", "Frisby Premium"))
                                        .expectNextMatches(f -> f.getName().equals("Frisby Premium"))
                                        .verifyComplete();
                }

                @Test
                @DisplayName("Debe lanzar DuplicateNameException cuando el nombre ya existe")
                void shouldThrowWhenNameDuplicate() {
                        Franchise franchise = sampleFranchise();

                        when(franchiseRepository.findById("franc-1")).thenReturn(Mono.just(franchise));
                        when(franchiseRepository.existsByName("KFC")).thenReturn(Mono.just(true));

                        StepVerifier.create(franchiseUseCase.updateFranchiseName("franc-1", "KFC"))
                                        .expectError(DuplicateNameException.class)
                                        .verify();

                        // Nunca se llama a updateFranchiseName si el nombre ya está en uso
                        verify(franchiseRepository, never())
                                        .updateFranchiseName(anyString(), anyString());
                }

        @Test
        @DisplayName("Debe lanzar FranchiseNotFoundException cuando franquicia no existe")
        void shouldThrowWhenFranchiseNotFound() {
            when(franchiseRepository.findById("no-existe")).thenReturn(Mono.empty());

            StepVerifier.create(franchiseUseCase.updateFranchiseName(
                            "no-existe", "Nuevo Nombre"))
                    .expectError(FranchiseNotFoundException.class)
                    .verify();
        }
        }
        @Nested
        @DisplayName("addBranch")
        class AddBranch {

                @Test
                @DisplayName("Debe agregar sucursal cuando franquicia existe y nombre no está duplicado")
                void shouldAddBranchSuccessfully() {
                        Franchise franchise = buildFranchise("franc-1", "Frisby", new ArrayList<>());
                        Franchise updated = buildFranchise("franc-1", "Frisby",
                                        List.of(buildBranch("branch-1", "Sucursal Norte", new ArrayList<>())));

                        when(franchiseRepository.findById("franc-1")).thenReturn(Mono.just(franchise));
                        when(franchiseRepository.addBranch("franc-1", "Sucursal Norte"))
                                        .thenReturn(Mono.just(updated));

                        StepVerifier.create(franchiseUseCase.addBranch("franc-1", "Sucursal Norte"))
                                        .expectNextMatches(f -> f.getBranches().size() == 1
                                                        && f.getBranches().get(0).getName().equals("Sucursal Norte"))
                                        .verifyComplete();

                        verify(franchiseRepository).addBranch("franc-1", "Sucursal Norte");
                }

        @Test
        @DisplayName("Debe lanzar FranchiseNotFoundException cuando la franquicia no existe")
        void shouldThrowWhenFranchiseNotFound() {
            // switchIfEmpty en el UseCase convierte Mono.empty() en un error
            when(franchiseRepository.findById("no-existe")).thenReturn(Mono.empty());

            StepVerifier.create(franchiseUseCase.addBranch("no-existe", "Sucursal"))
                    .expectError(FranchiseNotFoundException.class)
                    .verify();

            // Nunca se debe intentar agregar la sucursal si la franquicia no existe
            verify(franchiseRepository, never()).addBranch(anyString(), anyString());
        }

                @Test
                @DisplayName("Debe lanzar DuplicateNameException cuando la sucursal ya existe (case-insensitive)")
                void shouldThrowWhenBranchNameDuplicate() {
                        Branch existing = buildBranch("branch-1", "Sucursal Norte", new ArrayList<>());
                        Franchise franchise = buildFranchise("franc-1", "Frisby",
                                        new ArrayList<>(List.of(existing)));

                        when(franchiseRepository.findById("franc-1")).thenReturn(Mono.just(franchise));

                        // "sucursal norte" (minúscula) debe chocar con "Sucursal Norte" existente
                        StepVerifier.create(franchiseUseCase.addBranch("franc-1", "sucursal norte"))
                                        .expectError(DuplicateNameException.class)
                                        .verify();

                        verify(franchiseRepository, never()).addBranch(anyString(), anyString());
                }
        }
}
