package com.curelingo.curelingo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CurelingoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurelingoApplication.class, args);
	}

}
