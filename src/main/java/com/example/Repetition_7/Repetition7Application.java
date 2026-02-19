package com.example.Repetition_7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Repetition7Application {

	public static void main(String[] args) {
		SpringApplication.run(Repetition7Application.class, args);
	}

}
