package com.example.testaglibrarymanager.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.testaglibrarymanager.model.dto.BorrowerDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BorrowerEventProducer {
    private final KafkaTemplate<String, BorrowerDto> kafkaTemplate;

    public void publishBorrowerEvent(String topic, BorrowerDto borrowEvent) {
        log.info("Publish event to topic {} {}", topic, borrowEvent);
        kafkaTemplate.send(topic, borrowEvent.id().toString(), borrowEvent).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message published successfully to topic {} [Partition {}] [Offset {}]",
                        topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            } else {
                log.error("Message publish failed to topic: {}, error: ", topic, ex.getMessage());
            }
        });
    }
}
