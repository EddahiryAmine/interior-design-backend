package com.ace.media_service;

import com.ace.media_service.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MediaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediaServiceApplication.class, args);
	}
}
