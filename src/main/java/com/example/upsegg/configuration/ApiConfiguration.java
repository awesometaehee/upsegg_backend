package com.example.upsegg.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "api")
@Data
public class ApiConfiguration {
	// yml의 api:
	// base-path:
	private String basePath;
}
