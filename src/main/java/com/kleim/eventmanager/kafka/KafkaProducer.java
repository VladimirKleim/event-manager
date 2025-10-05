package com.kleim.eventmanager.kafka;

import com.kleim.eventmanager.notification.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class KafkaProducer {

    private final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<Long, NotificationEvent> kafkaTemplate;

    @Value("${kafka.notification-topic}")
    private String notificationTopic;

    public KafkaProducer(KafkaTemplate<Long, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessage(NotificationEvent kafkaMessage) {
        log.info("Kafka has start send message: {}", kafkaMessage);
        var result = kafkaTemplate.send(
                notificationTopic,
                kafkaMessage.getEventId(),
                kafkaMessage
        );

        result.thenAccept(res -> log.info("Сообщение успешно проброшено в кафку. Сообщение: {}, id: {}", kafkaMessage, kafkaMessage.getEventId()));
    }
}
