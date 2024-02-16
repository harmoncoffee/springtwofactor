package com.pharmondev.springtwofactor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SpringtwofactorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringtwofactorApplication.class, args);
	}

}
