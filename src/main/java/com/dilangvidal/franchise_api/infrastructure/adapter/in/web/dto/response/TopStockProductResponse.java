package com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.response;

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
        name = "TopStockProductResponse",
        description = "Respuesta que representa el producto con mayor stock por sucursal"
)
public class TopStockProductResponse {

    @Schema(
            description = "ID de la sucursal",
            example = "64b1f2e3c2a4e12d3f456790"
    )
    private String branchId;

    @Schema(
            description = "Nombre de la sucursal",
            example = "Sucursal Norte - Barranquilla"
    )
    private String branchName;

    @Schema(
            description = "ID del producto con mayor stock en la sucursal",
            example = "64b1f2e3c2a4e12d3f456791"
    )
    private String productId;

    @Schema(
            description = "Nombre del producto con mayor stock",
            example = "Familiar Frisby francesa"
    )
    private String productName;

    @Schema(
            description = "Valor de stock del producto estrella",
            example = "350"
    )
    private Integer stock;
}
