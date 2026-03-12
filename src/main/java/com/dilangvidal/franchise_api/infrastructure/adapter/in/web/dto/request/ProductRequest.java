package com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "ProductRequest", description = "Solicitud del payload para crear un producto en una sucursal")
public record ProductRequest(

        @NotBlank(message = "El nombre del producto no debe estar en blanco")
        @Size(min = 2, max = 100, message = "El nombre del producto debe tener entre 2 y 100 caracteres!")
        @Schema(description = "Nombre del producto", example = "Familiar Frisby francesa",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @NotNull(message = "El stock no puede ser nulo")
        @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
        @Schema(description = "Stock inicial del producto", example = "150",
                requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
        Integer stock
) {}