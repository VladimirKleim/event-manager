package com.kleim.eventmanager.mapper;

import com.kleim.eventmanager.location.domain.Location;
import com.kleim.eventmanager.location.domain.LocationDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface LocationMapper {

    Location toDomain(LocationDto locationDto);

    LocationDto toDtoLocation(Location location);

}
