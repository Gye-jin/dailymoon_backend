package com.example.dailymoon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DailymoonApplication {


	public static void main(String[] args) {
		SpringApplication.run(DailymoonApplication.class, args);
	}

}
