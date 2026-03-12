package com.dilangvidal.franchise_api.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Sucursal",
        description = "Una sucursal que pertenece a una franquicia y contiene una lista de productos."
)
public class Branch {

    @Schema(
            description = "Identificador unico por sucursal",
            example = "uuid",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private String id;

    @Schema(
            description = "Nombre de la sucursal",
            example = "Sucursal Norte - Barranquilla",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 2,
            maxLength = 100
    )
    private String name;

    @Schema(
            description = "Listado de productos ofrecidos en esta sucursal.",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Builder.Default
    private List<Product> products = new ArrayList<>();
}