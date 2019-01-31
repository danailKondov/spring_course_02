package ru.otus.spring02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "ru.otus.spring02")
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}

