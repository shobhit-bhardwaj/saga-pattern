package com.shobhit.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfiguration {

	@Bean
	@Qualifier("payment")
	public WebClient paymentClient(@Value("${service.endpoint.payment}") String endpoint) {
		return WebClient.builder().baseUrl(endpoint).build();
	}

	@Bean
	@Qualifier("inventory")
	public WebClient inventoryClient(@Value("${service.endpoint.inventory}") String endpoint) {
		return WebClient.builder().baseUrl(endpoint).build();
	}
}