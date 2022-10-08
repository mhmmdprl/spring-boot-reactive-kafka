package demo.service;

import demo.ApplicationStartup;
import demo.dvo.FakeConsumerDTO;
import demo.dvo.FakeProducerDTO;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.Duration;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ReactiveConsumerService implements CommandLineRunner {


    private final ReactiveKafkaConsumerTemplate<String, FakeConsumerDTO> reactiveKafkaConsumerTemplate;

    private final ReactiveProducerService reactiveProducerService;

    private final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    private final Counter validMessageCounter;

    @Autowired
    public ReactiveConsumerService(ReactiveKafkaConsumerTemplate<String, FakeConsumerDTO> reactiveKafkaConsumerTemplate,
                                   ReactiveProducerService reactiveProducerService, MeterRegistry registry) {
        this.reactiveKafkaConsumerTemplate = reactiveKafkaConsumerTemplate;
        this.reactiveProducerService = reactiveProducerService;
        this.validMessageCounter = Counter.builder("poller.messages")
            .tag("type", "valid")
            .description("Number of valid messages consumed from kafka")
            .register(registry);
    }

    @Override
    public void run(String... args) throws Exception {
        Flux<FakeConsumerDTO> flux = consumeFakeConsumerDTO();
        Flux<List<FakeConsumerDTO>> fluxlist = flux.buffer(3);
        fluxlist.subscribe(this::sendData);

    }

    private void sendData(List<FakeConsumerDTO> fakeConsumerDTOS) {
        fakeConsumerDTOS.forEach(fakeConsumerDTO -> {
            this.reactiveProducerService.send(new FakeProducerDTO(fakeConsumerDTO.getId()));
        });
    }

    private Flux<FakeConsumerDTO> consumeFakeConsumerDTO() {
        return reactiveKafkaConsumerTemplate
            .receiveAutoAck()
            .delayElements(Duration.ofSeconds(2L)) // BACKPRESSURE
            .doOnNext(consumerRecord -> logger.info("received key={}, value={} from topic={}, offset={}",
                consumerRecord.key(),
                consumerRecord.value(),
                consumerRecord.topic(),
                consumerRecord.offset())
            ).metrics()
            .map(ConsumerRecord::value).filter(this::checkMessage)
            .doOnNext(fakeConsumerDTO -> logger.info("successfully consumed {}={}", FakeConsumerDTO.class.getSimpleName(), fakeConsumerDTO))
            .doOnError(throwable -> logger.error("something bad happened while consuming : {}", throwable.getMessage()));
    }

    private boolean checkMessage(FakeConsumerDTO fakeConsumerDTO) {
        this.validMessageCounter.increment();
        return true;
    }


}
