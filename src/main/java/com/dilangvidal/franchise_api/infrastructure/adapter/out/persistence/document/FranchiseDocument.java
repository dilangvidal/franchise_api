package com.dilangvidal.franchise_api.infrastructure.adapter.out.persistence.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @Data, @Builder, @NoArgsConstructor, @AllArgsConstructor: Anotaciones de Lombok que reducen el 
 * código boilerplate (generan getters, setters, constructores y patrón builder automáticamente).
 * @Document(collection = "franchises"): Indica a Spring Data MongoDB que esta clase
 * mapea a un documento dentro de la colección "franchises" en la base de datos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "franchises")
public class FranchiseDocument {
    // @Id indica que este campo es la clave primaria (identificador único) en MongoDB.
    @Id
    private String id;

    // @Indexed(unique = true) asegura que el valor de este campo (name) no se repita en la colección.
    @Indexed(unique = true)
    private String name;

    @Builder.Default
    private List<BranchDocument> branches = new ArrayList<>();
}