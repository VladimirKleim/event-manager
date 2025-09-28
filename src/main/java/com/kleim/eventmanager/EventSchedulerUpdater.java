package com.kleim.eventmanager;

import com.kleim.eventmanager.event.db.EventRepository;
import com.kleim.eventmanager.event.domain.EventStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@EnableScheduling
@Configuration
@Component
public class EventSchedulerUpdater {

    private final Logger log = LoggerFactory.getLogger(EventSchedulerUpdater.class);
    private final EventRepository eventRepository;

    public EventSchedulerUpdater(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    @Scheduled(cron = "${event.status.cron}")
    public void eventStatusUpdater() {

        log.info("Scheduler started");

        eventRepository.startEventWithStateWaitStart(EventStatus.WAIT_START, EventStatus.STARTED);

        eventRepository.startEventWithStateFinished(EventStatus.STARTED, EventStatus.FINISHED);



    }

}
