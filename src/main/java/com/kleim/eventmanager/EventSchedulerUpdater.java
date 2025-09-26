package com.kleim.eventmanager;

import com.kleim.eventmanager.event.db.EventRepository;
import com.kleim.eventmanager.event.domain.EventStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Configuration
@Component
public class EventSchedulerUpdater {

    private final Logger log = LoggerFactory.getLogger(EventSchedulerUpdater.class);
    private final EventRepository eventRepository;

    public EventSchedulerUpdater(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(cron = "${event.status.cron}")
    public void eventStatusUpdater() {

        log.info("Scheduler started");

        var foundStartedEvents = eventRepository.startEventWithStateWaitStart(EventStatus.WAIT_START);
        foundStartedEvents.forEach(event -> eventRepository.changeEventStatus(event.getId(), EventStatus.STARTED)
        );

        var  foundEndedEvents = eventRepository.startEventWithStateFinished(EventStatus.STARTED);
        foundStartedEvents.forEach(event -> eventRepository.changeEventStatus(event.getId(), EventStatus.FINISHED));


    }

}
