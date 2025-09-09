package com.kleim.eventmanager.repository;

import com.kleim.eventmanager.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {


}
