package com.kleim.eventmanager.repository;

import com.kleim.eventmanager.entity.EventEntity;
import com.kleim.eventmanager.model.event.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface EventRepository extends JpaRepository<EventEntity, Long> {


    @Transactional
    @Modifying
    @Query("""
           UPDATE EventEntity e
             SET e.status = :status
               WHERE e.id = :id
           """)
    void changeEventStatus(
           @Param("id")  Long eventId,
           @Param("status")  EventStatus eventStatus
    );


}
