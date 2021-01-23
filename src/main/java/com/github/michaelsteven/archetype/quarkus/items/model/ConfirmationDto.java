package com.github.michaelsteven.archetype.quarkus.items.model;

import java.time.ZonedDateTime;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;


/**
 * Instantiates a new confirmation dto.
 */
@Data
@Schema(name="Confirmation")
@RegisterForReflection
public class ConfirmationDto {
	
	/** The id. */
	private Long id;
	
	/** The status. */
	private ItemStatus status;
	
	/** The date submitted. */
	private ZonedDateTime dateSubmitted;
}
