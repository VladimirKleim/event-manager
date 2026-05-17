package com.kleim.eventmanager.location.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record LocationDto(

        @Null
        Long id,

        @Schema(name = "name", description = "Наименование местоположения")
        @Size(max=55)
        String name,

        @Schema(name = "address", description = "Актуальный адрес локации")
        @NotBlank
        String address,

        @Schema(name = "capacity", description = "Вместимость локации")
        @NotNull
        @Min(5)
        Integer capacity,

        @Schema(name = "description", description = "Описание локации")
        String description

) {}
