package com.alco.algorithmic;

import com.alco.algorithmic.service.FileService;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication()
@EnableAsync
public class AlgorithmicApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlgorithmicApplication.class, args);
	}

}
