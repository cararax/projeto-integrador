package com.carara.nursenow.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.carara.nursenow.domain")
@EnableJpaRepositories("com.carara.nursenow.repos")
@EnableTransactionManagement
public class DomainConfig {
}
