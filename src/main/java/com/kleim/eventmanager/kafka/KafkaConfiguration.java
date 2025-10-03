package com.kleim.eventmanager.kafka;

import com.kleim.eventmanager.notification.EventChangeKafkaMessage;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


@Configuration
public class KafkaConfiguration {
    @Bean
    public KafkaTemplate<Long, EventChangeKafkaMessage> template(
            KafkaProperties kafkaProperties
    ) {
        var pros = kafkaProperties.buildProducerProperties(
                new DefaultSslBundleRegistry()
        );
        ProducerFactory<Long, EventChangeKafkaMessage> producerFactory =
                new DefaultKafkaProducerFactory<>(pros);

        return new KafkaTemplate<>(producerFactory);
    }
}
