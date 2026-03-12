package com.dilangvidal.franchise_api.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Producto",
        description = "Un producto ofrecido en una sucursal con su stock actual"
)
public class Product {

    @Schema(
            description = "Identificador unico del producto",
            example = "uuid",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private String id;

    @Schema(
            description = "Nombre del producto",
            example = "Noches Felices Nashville",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 2,
            maxLength = 100
    )
    private String name;

    @Schema(
            description = "Unidades disponibles en stock para este producto",
            example = "150",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0"
    )
    private Integer stock;
}