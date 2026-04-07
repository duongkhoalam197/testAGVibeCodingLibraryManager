package com.example.testaglibrarymanager.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.testaglibrarymanager.config.KafkaConfig;
import com.example.testaglibrarymanager.model.dto.BorrowTicketDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TicketEventConsumer {

    @KafkaListener(topics = { KafkaConfig.TICKET_BORROW_TOPIC }, groupId = "library-management-group")
    public void consumeBorrowTicketEvent(BorrowTicketDto borrowTicketEvent) {
        log.info("Received ticket event from Kafka with event type: {}, ticket id: {}, borrower id: {}, timestamp: {}",
                borrowTicketEvent.eventType(),
                borrowTicketEvent.id(),
                borrowTicketEvent.borrowerId(),
                borrowTicketEvent.eventDateTime());
    }
}
