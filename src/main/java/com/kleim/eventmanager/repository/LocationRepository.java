package com.kleim.eventmanager.repository;

import com.kleim.eventmanager.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

}
