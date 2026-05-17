package com.kleim.eventmanager.mapper;

import com.kleim.eventmanager.event.domain.Event;
import com.kleim.eventmanager.event.domain.EventDto;
import com.kleim.eventmanager.event.domain.EventRegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface EventMapper {

    @Mapping(target = "registrationList", expression = "java(emptyList())")
    Event toDomain(EventDto eventDto);

    @Mapping(target = "occupiedPlace", expression = "java(getOccupiedListSize(event.registrationList()))")
    EventDto toDto(Event event);

    default <T> List<T> emptyList() {
        return new ArrayList<>();
    }
    default Integer getOccupiedListSize(List<EventRegistration> registrationList) {
        return registrationList == null ? 0 : registrationList.size();
    }
}
