package com.kleim.eventmanager.repository;

import com.kleim.eventmanager.entity.LocationEntity;
import com.kleim.eventmanager.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {


}
