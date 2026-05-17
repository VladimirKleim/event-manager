package com.kleim.eventmanager.location.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kleim.eventmanager.location.domain.LocationDto;
import com.kleim.eventmanager.middleware.ServerErrorMessage;
import com.kleim.eventmanager.redis.CacheMode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Location")
@RequestMapping("api/location")
@SecurityRequirement(name = "bearerAuth")
public interface LocationApi {

    @Operation(
            summary = "Создать локацию",
            description = "Создаёт новую локацию. Доступно только ADMIN. Поле id должно быть null."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Локация успешно создана",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LocationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невалидные данные или id передан при создании",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "JWT токен отсутствует или невалиден",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            )
    })
    @PostMapping
    ResponseEntity<LocationDto> createLocation(
            @RequestBody @Valid LocationDto location
    );

    @Operation(summary = "Получить локацию по id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "202",
                    description = "Локация найдена",
                    content = @Content(schema = @Schema(implementation = LocationDto.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Локация не найдена",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
            @ApiResponse(responseCode = "401",
                    description = "Не авторизирован",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
            @ApiResponse(responseCode = "403",
                    description = "Недостаточно прав",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            )
    })
    @GetMapping("/{id}")
    ResponseEntity<LocationDto> getLocationById(
            @PathVariable("id") Long id,
            @Parameter(description = "Режим кеширования", example = "NONE_CACHE")
            @RequestParam(value = "cacheMode", defaultValue = "NONE_CACHE") CacheMode cacheMode
    ) throws JsonProcessingException;

    @Operation(summary = "Получить список всех локаций")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "202",
                    description = "Локации найдены",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не удалось выгрузить все локации",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            )
    })
    @GetMapping
    List<LocationDto> getAllLocation();

    @Operation(summary = "Пометить локацию как удаленную")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Локация удалена"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Локация не найдена или нельзя удалить",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteLocation(
            @PathVariable("id") Long id,
            @RequestParam(value = "cacheMode", defaultValue = "NONE_CACHE") CacheMode cacheMode
    );

    @Operation(description = "Обновление локации по id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Локация была обновлена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LocationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не удалось обновить локацию",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ServerErrorMessage.class))
            )
    })
    @PutMapping("/{id}")
    ResponseEntity<LocationDto> updateLocation(
            @PathVariable("id") Long id,
            @RequestBody LocationDto locationDto,
            @RequestParam(value = "cacheMode", defaultValue = "NONE_CACHE") CacheMode cacheMode
    );
}
