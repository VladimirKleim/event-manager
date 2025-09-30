package com.kleim.eventmanager.kafka;

import com.kleim.eventmanager.event.EventChangeKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<Long, EventChangeKafkaMessage> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<Long, EventChangeKafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessage(EventChangeKafkaMessage kafkaMessage) {
        log.info("Kafka has start send message: {}", kafkaMessage);
      var result = kafkaTemplate.send(
              "event-notification",
                    kafkaMessage
      );

      result.thenAccept(res -> log.info("Send successful"));
    }

}
