package com.example.testaglibrarymanager.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.testaglibrarymanager.config.KafkaConfig;
import com.example.testaglibrarymanager.model.dto.CategoryDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryEventConsumer {

    @KafkaListener(topics = {
            KafkaConfig.CATEGORY_CREATE_TOPIC,
            KafkaConfig.CATEGORY_DELETE_TOPIC,
            KafkaConfig.CATEGORY_UPDATED_TOPIC
    }, groupId = "library-management-group")
    public void consumeCategoryEvent(CategoryDto categoryEvent) {
        log.info("Received category event from Kafka with event type: {}, category id: {}, timestamp: {}",
                categoryEvent.eventType(), categoryEvent.id(), categoryEvent.eventTimestamp());
    }
}
