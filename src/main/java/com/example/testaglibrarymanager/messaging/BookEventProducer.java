package com.example.testaglibrarymanager.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.testaglibrarymanager.model.dto.BookEventDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookEventProducer {
    private final KafkaTemplate<String, BookEventDto> kafkaTemplate;

    public void publishBookEvent(String topic, BookEventDto bookEvent) {
        log.info("Publishing event to topic {}: {}", topic, bookEvent);
        kafkaTemplate.send(topic, bookEvent.bookId().toString(), bookEvent)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message sent successfully to topic {} [Partition {}]  [Offset {}]",
                                topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                    } else {
                        log.info("Message sent fail to topic {}, failed messsage: {}",
                                topic, ex.getMessage());
                    }
                });
    }
}
