package com.spring.dailymoon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.spring.dailymoon.service.MemberService;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class DailymoonApplication {
	@Autowired
	MemberService memberservice;
	
	public static void main(String[] args) {
		SpringApplication.run(DailymoonApplication.class, args);
	}
}
