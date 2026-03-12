package com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "BranchRequest", description = "Request payload crear o actualizar sucursal")
public record BranchRequest (
        @NotBlank(message = "El nombre de la sucursal no debe estar en blanco")
        @Size(min = 2, max = 100, message = "El nombre de la sucursal debe tener entre 2 y 100 caracteres!")
        @Schema(description = "Nombre de la sucursal", example = "Sucursal Norte - Barranquilla",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String name
) {}
