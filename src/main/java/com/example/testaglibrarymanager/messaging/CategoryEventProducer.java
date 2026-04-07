package com.example.testaglibrarymanager.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.testaglibrarymanager.model.dto.CategoryDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryEventProducer {
    private final KafkaTemplate<String, CategoryDto> kafkaTemplate;

    public void publishCategoryEvent(String topic, CategoryDto categoryEvent) {
        log.info("Publishing event to topic {} {} ", topic, categoryEvent);
        kafkaTemplate.send(topic, categoryEvent.id().toString(), categoryEvent).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent successfully to topic: {} [Parition {}] [Offset {}]",
                        topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            } else {
                log.error("Message sent failed to topic: {}, failed message: {}", topic, ex.getMessage());
            }
        });
    }
}
