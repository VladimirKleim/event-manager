package com.kleim.eventmanager.event.db;

import com.kleim.eventmanager.event.domain.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<EventEntity, Long> {


    @Modifying
    @Query("""
          UPDATE EventEntity e
          SET e.status = :status
             WHERE e.id = :id
          """)
    void changeEventStatus(
          @Param("id")  Long eventId,
          @Param("status")  EventStatus status
    );


    @Modifying(clearAutomatically = true)
    @Query(value = """
           UPDATE EventEntity e SET
           e.name = COALESCE(:name, e.name),
           e.maxPlace = COALESCE(:maxPlace, e.maxPlace),
           e.date = COALESCE(:date, e.date),
           e.cost = COALESCE(:cost, e.cost),
           e.duration = COALESCE(:duration, e.duration),
           e.locationId = COALESCE(:locationId, e.locationId)
            WHERE e.id = :id
           """)
    void updateEvent(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("maxPlace") Integer maxPlace,
            @Param("date") LocalDateTime date,
            @Param("cost") Integer cost,
            @Param("duration") Integer duration,
            @Param("locationId") Long locationId
    );


    @Query("""
          SELECT e FROM EventEntity e WHERE
          (:name IS NULL OR e.name LIKE%:name%) AND
          (:placesMin IS NULL OR e.maxPlace <= :placesMin) AND
          (:placesMax IS NULL OR e.maxPlace >= :placesMax) AND
          (CAST(:dateStartAfter AS DATE) IS NULL OR e.date <= :dateStartAfter) AND
          (CAST(:dateStartBefore AS DATE) IS NULL OR e.date >= :dateStartBefore) AND
          (:costMin IS NULL OR e.cost <= :costMin) AND
          (:costMax IS NULL OR e.cost >= :costMax) AND
          (:durationMin IS NULL OR e.duration <= :durationMin) AND
          (:durationMax IS NULL OR e.duration >= :durationMax) AND
          (:locationId IS NULL OR e.locationId = :locationId) AND
          (:status IS NULL OR e.status = :status)
          """)
    List<EventEntity> searchAllEventsByFilter(
            @Param("name") String name,
            @Param("placesMin")  Integer placesMin,
            @Param("placesMax") Integer placesMax,
            @Param("dateStartAfter") LocalDateTime dateStartAfter,
            @Param("dateStartBefore") LocalDateTime dateStartBefore,
            @Param("costMin")  BigDecimal costMin,
            @Param("costMax")  BigDecimal costMax,
            @Param("durationMin")  Integer durationMin,
            @Param("durationMax")  Integer durationMax,
            @Param("locationId")  Long locationId,
            @Param("status")   EventStatus status
    );


    List<EventEntity> findAllByOwnerId(
           Long ownerId
    );


    @Modifying
    @Query("""
            UPDATE EventEntity e
            SET e.status = :newStatus
            WHERE e.status = :oldStatus AND e.date <= CURRENT_TIMESTAMP
            """)
    void startEventWithStateWaitStart(
            @Param("oldStatus") EventStatus oldStatus,
            @Param("newStatus") EventStatus newStatus
    );


    @Modifying
    @Query(value = """
          UPDATE events e
          SET status = :newStatus WHERE status = :oldStatus AND
              e.date + INTERVAL '1 minute' * e.duration < NOW()
          """, nativeQuery = true)
    void startEventWithStateFinished(
            @Param("oldStatus") EventStatus oldStatus,
            @Param("newStatus") EventStatus newStatus
    );
}
