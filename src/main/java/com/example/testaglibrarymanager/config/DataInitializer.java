package com.example.testaglibrarymanager.config;

import com.example.testaglibrarymanager.model.entity.Borrower;
import com.example.testaglibrarymanager.repository.CategoryRepository;
import com.example.testaglibrarymanager.repository.BorrowerRepository;
import com.example.testaglibrarymanager.model.entity.Category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(CategoryRepository categoryRepository, BorrowerRepository borrowerRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                log.info("Initializing fake Category data...");
                categoryRepository.saveAll(List.of(
                        new Category("IT / Tech"),
                        new Category("Novel"),
                        new Category("Science"),
                        new Category("History")
                ));
            }

            if (borrowerRepository.count() == 0) {
                log.info("Initializing fake Borrower data...");
                borrowerRepository.saveAll(List.of(
                        new Borrower("Nguyen Van A", "a@example.com", "0901234567"),
                        new Borrower("Tran Thi B", "b@example.com", "0987654321")
                ));
            }
        };
    }
}

