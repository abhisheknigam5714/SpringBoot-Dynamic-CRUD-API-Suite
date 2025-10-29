package com.codehunt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrudMiniProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudMiniProjectApplication.class, args);
		System.out.println("connection done?");
	}

}
