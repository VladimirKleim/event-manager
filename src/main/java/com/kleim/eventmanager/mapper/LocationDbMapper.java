package com.kleim.eventmanager.mapper;

import com.kleim.eventmanager.location.db.LocationEntity;
import com.kleim.eventmanager.location.domain.Location;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface LocationDbMapper {

    LocationEntity toEntity(Location location);

    Location toLocation(LocationEntity location);
}
