package com.kleim.eventmanager.event.db;

import com.kleim.eventmanager.event.domain.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
           @Param("status") EventStatus eventStatus
    );



    @Modifying
    @Transactional
    @Query("""
           UPDATE EventEntity e SET e.name = :name,
            e.maxPlace = :maxPlace, e.date = :date, e.cost = :cost, e.duration = :duration, e.locationId = :locationId
           WHERE e.id = :id
           """)
    EventEntity updateEvent(
            @Param("name") String name,
            @Param("maxPlace") Integer maxPlace,
            @Param("date") LocalDateTime date,
            @Param("cost") Integer cost,
            @Param("duration") Integer duration,
            @Param("locationId") Long locationId
    );


}
