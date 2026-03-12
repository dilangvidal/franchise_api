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
        name = "Franquicia",
        description = "Está compuesta por un nombre y una lista de sucursales."
)
public class Franchise {
    @Schema(
            description = "Identificador único de la franquicia",
            example = "uuid",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private String id;

    @Schema(
            description = "Nombre de la franquicia",
            example = "Frisby",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 2,
            maxLength = 100
    )
    private String name;

    @Schema(
            description = "Listado de sucursales pertenecientes a esta franquicia",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Builder.Default
    private List<Branch> branches = new ArrayList<>();

}
