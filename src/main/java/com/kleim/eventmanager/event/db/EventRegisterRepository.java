package com.kleim.eventmanager.event.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRegisterRepository extends JpaRepository<EventRegisterEntity, Long> {

    @Query("""
          SELECT re FROM EventRegisterEntity re
          WHERE re.event.id = :eventId AND re.userId = :userId
          """)
    Optional<EventRegisterEntity> findByEventIdAndUserId(
          Long userId,
          Long eventId
    );

    @Query("""
          SELECT e.event FROM EventRegisterEntity e
          WHERE e.userId = :userId
          """)
    List<EventEntity> findEvents(
            @Param("userId") Long userId
    );
}
