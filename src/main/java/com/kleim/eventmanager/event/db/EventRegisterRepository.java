package com.kleim.eventmanager.event.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRegisterRepository extends JpaRepository<EventRegisterEntity, Long> {

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

    @Query(value = """
           SELECT u.login FROM event_register r JOIN users AS u
           ON r.user_id = u.id WHERE r.event_id = :eventId
           """
            , nativeQuery = true)
    List<String> findAllUserLoginsByEvent(Long eventId);
}
