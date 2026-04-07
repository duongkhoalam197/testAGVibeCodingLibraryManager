package com.example.testaglibrarymanager.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.testaglibrarymanager.config.KafkaConfig;
import com.example.testaglibrarymanager.model.dto.BorrowerDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BorrowerEventConsumer {
    @KafkaListener(topics = {
            KafkaConfig.BORROWER_CREATE_TOPIC,
            KafkaConfig.BORROWER_DELETE_TOPIC,
            KafkaConfig.BORROWER_UPDATE_TOPIC }, groupId = "library-management-group")
    public void consumeBorrowerEvent(BorrowerDto borrowerDto) {
        log.info("Received borrower event from Kafka with event type: {}, borrower id: {}, timestamp: {}",
                borrowerDto.eventType(), borrowerDto.id(), borrowerDto.eventTimestamp());
    }
}
