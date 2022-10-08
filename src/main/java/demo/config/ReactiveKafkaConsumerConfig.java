package demo.config;

import java.util.Collections;
import demo.dvo.FakeConsumerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

@Configuration
public class ReactiveKafkaConsumerConfig {

    @Bean
    public ReceiverOptions<String, FakeConsumerDTO> kafkaReceiverOptions(@Value(value = "${FAKE_CONSUMER_DTO_TOPIC}") String topic,
                                                                         KafkaProperties kafkaProperties) {
        ReceiverOptions<String, FakeConsumerDTO> basicReceiverOptions = ReceiverOptions.create(kafkaProperties.buildConsumerProperties());
        return basicReceiverOptions.subscription(Collections.singletonList(topic));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, FakeConsumerDTO> reactiveKafkaConsumerTemplate(
        ReceiverOptions<String, FakeConsumerDTO> kafkaReceiverOptions ) {
        return new ReactiveKafkaConsumerTemplate<String, FakeConsumerDTO>(kafkaReceiverOptions);
    }
}
