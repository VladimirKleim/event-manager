package com.kleim.eventmanager.event.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRegister extends JpaRepository<EventRegisterEntity, Long> {
}
