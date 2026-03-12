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
@Schema(name = "FranchiseResponse", description = "Obteniendo información de la franquicia")
public class FranchiseResponse {

    @Schema(example = "64b1f2e3c2a4e12d3f456789")
    private String id;

    @Schema(example = "Frisby")
    private String name;

    private List<BranchResponse> branches;
}