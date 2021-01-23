package com.github.michaelsteven.archetype.quarkus.items.configuration;

import javax.inject.Singleton;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.jackson.ObjectMapperCustomizer;

/**
 * The Class JsonConfig.
 */
@Singleton
public class JsonConfig implements ObjectMapperCustomizer {

	/**
	 * Customize.
	 *
	 * @param objectMapper the object mapper
	 */
	@Override
	public void customize(ObjectMapper objectMapper) {
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		
	}

}
