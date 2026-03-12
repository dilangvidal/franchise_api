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

}
