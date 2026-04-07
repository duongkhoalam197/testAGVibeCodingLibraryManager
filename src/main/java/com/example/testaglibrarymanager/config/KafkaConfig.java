package com.example.testaglibrarymanager.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    public static final String BOOK_CREATED_TOPIC = "book.created";
    public static final String BOOK_UPDATED_TOPIC = "book.updated";
    public static final String BOOK_DELETED_TOPIC = "book.deleted";

    public static final String BORROWER_CREATE_TOPIC = "borrower.created";
    public static final String BORROWER_UPDATE_TOPIC = "borrower.updated";
    public static final String BORROWER_DELETE_TOPIC = "borrower.deleted";

    public static final String CATEGORY_CREATE_TOPIC = "category.created";
    public static final String CATEGORY_DELETE_TOPIC = "category.deleted";
    public static final String CATEGORY_UPDATED_TOPIC = "category.updated";

    public static final String TICKET_BORROW_TOPIC = "ticket.borrowed";

    @Bean
    public NewTopic bookCreatedTopic() {
        return TopicBuilder.name(BOOK_CREATED_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic bookUpdatedTopic() {
        return TopicBuilder.name(BOOK_UPDATED_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic bookDeletedTopic() {
        return TopicBuilder.name(BOOK_DELETED_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic borrowerCreatedTopic() {
        return TopicBuilder.name(BORROWER_CREATE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic borrowerUpdatedTopic() {
        return TopicBuilder.name(BORROWER_UPDATE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic borrowerDeletedTopic() {
        return TopicBuilder.name(BORROWER_DELETE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic categoryCreatedTopic() {
        return TopicBuilder.name(CATEGORY_CREATE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic categoryUpdatedTopic() {
        return TopicBuilder.name(CATEGORY_UPDATED_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic categoryDeletedTopic() {
        return TopicBuilder.name(CATEGORY_DELETE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic borrowTicketTopic() {
        return TopicBuilder.name(TICKET_BORROW_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
