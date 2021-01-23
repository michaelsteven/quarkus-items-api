package com.github.michaelsteven.archetype.quarkus.items.model;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data
@NoArgsConstructor
@RegisterForReflection
public class ApiError {

	/** The status. */
	private Status status;
	
	/** The message. */
	private String message;
	
	/** The errors. */
	private List<String> errors;
	
    /**
     * Instantiates a new api error.
     *
     * @param status the status
     * @param message the message
     * @param errors the errors
     */
    public ApiError(Status status, String message, List<String> errors) {
		this.status =  status;
		this.message = message;
		this.errors = errors;
	}
    
    /**
     * Instantiates a new api error.
     *
     * @param status the status
     * @param message the message
     * @param error the error
     */
    public ApiError(Status status, String message, String error) {
 		this.status =  status;
 		this.message = message;
 		this.errors = Arrays.asList(error);
 	}
}
