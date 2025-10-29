package com.codehunt.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Myconfig {
	
	@Bean
	public ModelMapper modelmap() {
		return new ModelMapper();
	}

}
