package com.qpark.eip.sample.boot.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAdminServer
@SpringBootApplication
public class SpringBootAdminServerApplication {

	public static void main(final String[] args) {
		SpringApplication.run(SpringBootAdminServerApplication.class, args);
	}
}
