package com.example.msasecondservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsaSecondServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsaSecondServiceApplication.class, args);
	}

}
