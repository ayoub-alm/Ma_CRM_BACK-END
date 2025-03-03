package com.sales_scout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@SpringBootApplication
//@EnableJpaAuditing(auditorAwareRef = "auditorAware") // ✅ Required for auditing
@EnableTransactionManagement
public class SalesScoutApplication {
	public static void main(String[] args) {
		SpringApplication.run(SalesScoutApplication.class, args);
	}
}
