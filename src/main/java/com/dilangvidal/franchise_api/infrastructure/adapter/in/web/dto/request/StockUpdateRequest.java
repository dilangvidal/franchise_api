package com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(name = "StockUpdateRequest", description = "Solicitando payload para actualizar el stock del producto")
public record StockUpdateRequest(

        @NotNull(message = "El stock no debe ser nulo")
        @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
        @Schema(description = "Nuevo valor de stock del producto", example = "200",
                requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
        Integer stock
) {}