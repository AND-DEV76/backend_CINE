package com.cinemaroyale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CinemaroyaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaroyaleApplication.class, args);
	}

}
 