package com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "BranchResponse", description = "Obteniendo información de la sucursal")
public class BranchResponse {

    @Schema(example = "64b1f2e3c2a4e12d3f456790")
    private String id;

    @Schema(example = "Sucursal Norte - Barranquilla")
    private String name;

    private List<ProductResponse> products;
}