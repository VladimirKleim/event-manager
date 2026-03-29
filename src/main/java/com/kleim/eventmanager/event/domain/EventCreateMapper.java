package com.kleim.eventmanager.event.domain;

import com.kleim.eventmanager.event.db.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;


@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        imports = {ArrayList.class, EventStatus.class}
)
public interface EventCreateMapper {

    EventCreateRequest toDomain(EventCreateRequestDto eventCreateRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", source = "userId")
    @Mapping(target = "registrationList", expression = "java(createEmptyList())")
    @Mapping(target = "status", expression = "java(EventStatus.WAIT_START)")
    EventEntity toEntity(Long userId, EventCreateRequest eventCreateRequest);

    //@Named("emptyRegistrationList")
    default <T> ArrayList<T> createEmptyList() {
        return new ArrayList<>();
    }
}
