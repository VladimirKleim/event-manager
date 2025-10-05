package com.kleim.eventmanager.kafka;

import com.kleim.eventmanager.notification.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class KafkaProducer {

    private final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<Long, NotificationEvent> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<Long, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessage(NotificationEvent kafkaMessage) {
        log.info("Kafka has start send message: {}", kafkaMessage);
        var result = kafkaTemplate.send(
                "event-notification",
                kafkaMessage.getEventId(),
                kafkaMessage
        );

        result.thenAccept(res -> log.info("Сообщение успешно проброшено в кафку. Сообщение: {}, id: {}", kafkaMessage, kafkaMessage.getEventId()));
    }
}
