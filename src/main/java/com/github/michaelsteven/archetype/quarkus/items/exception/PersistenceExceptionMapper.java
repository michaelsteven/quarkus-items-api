package com.github.michaelsteven.archetype.quarkus.items.exception;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.github.michaelsteven.archetype.quarkus.items.model.ApiError;

/**
 * The Class PersistenceExceptionMapper.
 */
@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {

	/**
	 * To response.
	 *
	 * @param exception the exception
	 * @return the response
	 */
	@Override
	public Response toResponse(PersistenceException exception) {
		exception.printStackTrace();
		String errorMessage = "Unable to retrieve or persist the item(s)";
        List<String> errors = new ArrayList<>();
        errors.add(errorMessage);
        ApiError apiError = new ApiError(Status.BAD_GATEWAY, errorMessage, errors);
        return Response.status(apiError.getStatus()).entity(apiError).build();
	}
}