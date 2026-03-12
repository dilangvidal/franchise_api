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
@Schema(name = "ProductResponse", description = "Información del producto")
public class ProductResponse {

    @Schema(example = "64b1f2e3c2a4e12d3f456791")
    private String id;

    @Schema(example = "Familiar Frisby francesa")
    private String name;

    @Schema(example = "150")
    private Integer stock;
}