package com.sales_scout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@SpringBootApplication
public class SalesScoutApplication {
	public static void main(String[] args) {
		SpringApplication.run(SalesScoutApplication.class, args);
	}
}
