package de.berlin.visa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VisaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VisaApplication.class, args);
	}
}
