package com.gapshap.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfiguration {

	@Value("${cloud.name}")
	private String cloudName;
	@Value("${cloud.api-key}")
	private String cloudApiKey;
	@Value("${cloud.secret-key}")
	private String apiSecretKey;

	
//	@Bean
//	public ObjectMapper objectMapper() {
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.registerModule(new JavaTimeModule());
//		return objectMapper;
//	}
//
//	
//	@Bean
//	public JsonMapper jsonMapper() {
//		JsonMapper jsonMapper = new JsonMapper();
//		jsonMapper.registerModule(new JavaTimeModule());
//		return jsonMapper;
//	}

	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(String.format("cloudinary://%s:%s@%s",cloudApiKey,apiSecretKey,cloudName ));
	
	}
	
	
	
	
}
