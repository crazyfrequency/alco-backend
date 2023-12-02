package com.alco.algorithmic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication()
@EnableAsync
public class AlgorithmicApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlgorithmicApplication.class, args);
	}

}
