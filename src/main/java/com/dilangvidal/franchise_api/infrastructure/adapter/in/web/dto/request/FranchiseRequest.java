package com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * @Schema: Anotación de OpenAPI (Swagger) utilizada para documentar la forma del modelo
 * en la interfaz interactiva de la API, explicando el propósito y formato de cada campo.
 */
@Schema(
        name = "Franquicia",
        description = "Solicitar payload para crear o actualizar una franquicia"
)
public record FranchiseRequest (
    @NotBlank(message = "El nombre de la franquicia no debe estar en blanco")
    @Size(min = 2, max = 100, message = "El nombre de la franquicia debe tener entre 2 y 100 caracteres")
    @Schema(
            description = "Nombre de la Franquicia",
            example = "Frisby",
            requiredMode = Schema.RequiredMode.REQUIRED)
     String name

){}