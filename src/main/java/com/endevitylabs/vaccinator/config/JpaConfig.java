package com.endevitylabs.vaccinator.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.endevitylabs.vaccinator.repository")
@EntityScan(basePackages = "com.endevitylabs.vaccinator.model")
@EnableTransactionManagement
public class JpaConfig {
} 