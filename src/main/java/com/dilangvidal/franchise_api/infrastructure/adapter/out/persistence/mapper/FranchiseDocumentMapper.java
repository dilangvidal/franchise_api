package com.dilangvidal.franchise_api.infrastructure.adapter.out.persistence.mapper;

import com.dilangvidal.franchise_api.domain.model.Branch;
import com.dilangvidal.franchise_api.domain.model.Franchise;
import com.dilangvidal.franchise_api.domain.model.Product;
import com.dilangvidal.franchise_api.infrastructure.adapter.out.persistence.document.BranchDocument;
import com.dilangvidal.franchise_api.infrastructure.adapter.out.persistence.document.FranchiseDocument;
import com.dilangvidal.franchise_api.infrastructure.adapter.out.persistence.document.ProductDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * @Component: Permite a Spring escanear esta clase y manejarla como un Bean,
 * de forma que pueda inyectarla donde sea requerida.
 */
@Component
public class FranchiseDocumentMapper {

    public Franchise toDomain(FranchiseDocument document) {
        return Franchise.builder()
                .id(document.getId())
                .name(document.getName())
                .branches(document.getBranches().stream()
                        .map(this::toDomain)
                        .toList())
                .build();
    }

    public Branch toDomain(BranchDocument document) {
        return Branch.builder()
                .id(document.getId())
                .name(document.getName())
                .products(document.getProducts().stream()
                        .map(this::toDomain)
                        .toList())
                .build();
    }

    public Product toDomain(ProductDocument document) {
        return Product.builder()
                .id(document.getId())
                .name(document.getName())
                .stock(document.getStock())
                .build();
    }

    public FranchiseDocument toDocument(Franchise domain) {
        return FranchiseDocument.builder()
                .id(domain.getId())
                .name(domain.getName())
                .branches(domain.getBranches().stream()
                        .map(this::toDocument)
                        .toList())
                .build();
    }

    public BranchDocument toDocument(Branch domain) {
        return BranchDocument.builder()
                // Asigna el id del dominio, o en caso de ser nulo (nuevo), genera uno automáticamente usando UUID.
                .id(domain.getId() != null ? domain.getId() : UUID.randomUUID().toString())
                .name(domain.getName())
                .products(domain.getProducts().stream()
                        .map(this::toDocument)
                        .toList())
                .build();
    }

    public ProductDocument toDocument(Product domain) {
        return ProductDocument.builder()
                .id(domain.getId() != null ? domain.getId() : UUID.randomUUID().toString())
                .name(domain.getName())
                .stock(domain.getStock())
                .build();
    }
}