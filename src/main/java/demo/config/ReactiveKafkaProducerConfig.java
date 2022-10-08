package demo.config;

import demo.dvo.FakeProducerDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
public class ReactiveKafkaProducerConfig {
    @Bean
    public ReactiveKafkaProducerTemplate<String, FakeProducerDTO> reactiveKafkaProducerTemplate(
        KafkaProperties properties) {
        Map<String, Object> props = properties.buildProducerProperties();
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new ReactiveKafkaProducerTemplate<String, FakeProducerDTO>(SenderOptions.create(props));
    }
}
