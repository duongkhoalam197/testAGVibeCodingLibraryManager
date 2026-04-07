package com.example.testaglibrarymanager.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.testaglibrarymanager.model.dto.BorrowTicketDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketEventProducer {
    private final KafkaTemplate<String, BorrowTicketDto> kafkaTemplate;

    public void publishBorrowTicketEvent(String topic, BorrowTicketDto borrowTicketEvent) {
        log.info("Publishing event to topic: {} {}", topic, borrowTicketEvent);
        kafkaTemplate.send(topic, borrowTicketEvent.bookId().toString(), borrowTicketEvent)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message published successfully to topic: {} [Partition: {}] [{}]",
                                topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                    } else {
                        log.error("Message published failed to topic: {}, error message: {}",
                                topic, ex.getMessage());
                    }
                });
    }
}
