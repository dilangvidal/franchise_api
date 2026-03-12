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
        name = "TopStockProduct",
        description = "Modelo de dominio que representa el producto con mayor stock por sucursal"
)
public class TopStockProduct {

    private String branchId;
    private String branchName;
    private String productId;
    private String productName;
    private Integer stock;
}