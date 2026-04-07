package com.example.testaglibrarymanager.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.example.testaglibrarymanager.config.KafkaConfig;
import com.example.testaglibrarymanager.model.dto.BookEventDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookEventConsumer {

    @KafkaListener(topics = {
            KafkaConfig.BOOK_CREATED_TOPIC,
            KafkaConfig.BOOK_DELETED_TOPIC,
            KafkaConfig.BOOK_UPDATED_TOPIC
    }, groupId = "library-management-group")
    public void consumeBookEvent(BookEventDto bookEvent) {
        log.info("Received book event from Kafka with event type: {}, book id: {}, timestamp: {}",
                bookEvent.eventType(), bookEvent.bookId(), bookEvent.eventTimestamp());
    }

}
