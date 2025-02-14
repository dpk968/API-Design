package com.dpk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaRegisteryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaRegisteryApplication.class, args);
	}

}
