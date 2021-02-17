package com.github.michaelsteven.archetype.quarkus.items.interceptor;

import java.util.Arrays;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Trace log interceptor.  Logs the entry and exit of methods for classess that are annotated with @TraceLog
 * Priority 10 is an arbitrary priority, change as needed. 
 *
 */
@Priority(10)
@TraceLog
@Interceptor
public class TraceLogInterceptor {

	
	/** The object mapper. */
	private ObjectMapper objectMapper;
	
	/**
	 * Constructor.
	 *
	 * @param objectMapper the object mapper
	 */
	public TraceLogInterceptor(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	
	/**
	 * Trace method.
	 *
	 * @param ctx the ctx
	 * @return the object
	 * @throws Exception the exception
	 */
	@AroundInvoke
	public Object traceMethod(InvocationContext ctx) throws Exception {
		Logger logger = this.getLogger(ctx);
		
		logger.info("Entered: {} () with arguments = {}" , 
				ctx.getMethod().getName(),
				Arrays.deepToString(ctx.getParameters()));
	    try {
	    	Object object = ctx.proceed();
	    	logger.info("Exited: {} () with result = {}", 
	    			ctx.getMethod().getName(), objectMapper.writeValueAsString(object)
	    		);
	    	return object;
	    } catch( Exception e) {
	    	logger.error("Exception {} in {}()", Arrays.toString(ctx.getParameters()),
	    			ctx.getMethod().getName()
	    		);
	    	throw e;
	    }
	}
	
	
	/**
	 * Gets the logger.
	 *
	 * @param ctx the ctx
	 * @return the logger
	 */
	Logger getLogger(InvocationContext ctx) {
		return LoggerFactory.getLogger(ctx.getTarget().getClass());
	}
}
