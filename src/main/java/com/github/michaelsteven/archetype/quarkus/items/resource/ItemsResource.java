package com.github.michaelsteven.archetype.quarkus.items.resource;

import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.github.michaelsteven.archetype.quarkus.items.interceptor.TraceLog;
import com.github.michaelsteven.archetype.quarkus.items.model.ApiError;
import com.github.michaelsteven.archetype.quarkus.items.model.ConfirmationDto;
import com.github.michaelsteven.archetype.quarkus.items.model.ItemDto;
import com.github.michaelsteven.archetype.quarkus.items.service.ItemsService;


/**
 * The Class ItemsResource.
 */
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "jwt")
@Tag(name = "Items", description = "The items api can be used to perform actions on Items")
@Path("/api/v1/items")
@TraceLog
public class ItemsResource {
	
	//public ItemsResource(ItemsService itemsService, MessageSource messageSource) {
	//	this.itemsService = itemsService;
	//	this.messageSource = messageSource;
	//}
	
	/** The items service. */
	@Inject
	ItemsService itemsService;
	
	/** The message source. */
	//private MessageSource messageSource;
	
	
    /**
     * Gets the items.
     *
     * @param pageable the pageable
     * @return the items
     */
    @Operation(summary = "Retrieve items", description = "Use this API to retrieve a paginated collection of items.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = PageImpl.class))),
            @APIResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @APIResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ApiError.class))) })
    @SecurityRequirement(name = "jwt", scopes = {})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems(@QueryParam @DefaultValue("0") int page, @QueryParam @DefaultValue("10") int size,  @QueryParam String[] sort){
    	Pageable pageable = PageRequest.of(page, size , Sort.by(sort));
    	Page<ItemDto> itemDtoPage = itemsService.getItems(pageable);
    	return Response.status(200).entity(itemDtoPage).build();
	}
    
    
    /**
     * Save item.
     *
     * @param itemDto the item dto
     * @return the response entity
     */
    @Operation(summary = "Submit a new item", description = "Use this API to generate a new item. "
            + "In some cases this POST method may return a 202 ACCEPTED response code, "
            + "in which case the data returned will contain a status code, and an identifier. "
            + "The identifier can then be used in subsequent GET calls to obtain the item at a later time.")
    @APIResponses(value = {
            @APIResponse(responseCode = "202", description = "accepted", content = @Content(schema = @Schema(implementation = ItemDto.class))),
            @APIResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @APIResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ApiError.class))) })
    //@PostMapping(API_PATH)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveItem(@Valid @RequestBody @Parameter(description = "A new item", required = true) ItemDto itemDto){
    	ConfirmationDto confirmationDto = itemsService.saveItem(itemDto);
    	return Response.status(202).entity(confirmationDto).build();
    }
    
    
    /**
     * Gets the item by id.
     *
     * @param id the id
     * @return the item by id
     */
    @Operation(summary = "Gets an item", description = "Use this API to retrieve an existing item.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ItemDto.class))),
            @APIResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @APIResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = Void.class))),
            @APIResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ApiError.class))) })
    @SecurityRequirement(name = "jwt", scopes = {})
    //@GetMapping(API_PATH + "/{id}")
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemById(@PathParam Long id){
    	Optional<ItemDto> optionalResponse = itemsService.getItemById(id);
    	return optionalResponse.map(response -> Response.ok(response).build())
    			.orElse(Response.status(404).build());
    }
    
    
    /**
     * Edits the item.
     *
     * @param id the id
     * @param itemDto the item dto
     * @return the response entity
     */
    @Operation(summary = "Modifies an item", description = "Use this API to modify an item. "
            + "In some cases this PUT method may return a 202 ACCEPTED response code, "
            + "in which case the data returned will contain a status code, and an identifier. "
            + "The identifier can then be used in subsequent GET calls to obtain the item at a later time.")
    @APIResponses(value = {
            @APIResponse(responseCode = "202", description = "accepted", content = @Content(schema = @Schema(implementation = ConfirmationDto.class))),
            @APIResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @APIResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ApiError.class))) })
    @SecurityRequirement(name = "jwt", scopes = {})
    //@PutMapping(API_PATH + "/{id}")
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editItem(@PathParam long id, @Valid @RequestBody @Parameter(description = "A modified item", required = true) ItemDto itemDto){
    	if( itemDto.getId() != null && id != itemDto.getId().longValue()) {
    		//String message = messageSource.getMessage("itemscontroller.validationexception.pathiddoesntmatchobject", 
    		//		new Object[] { String.valueOf(id), String.valueOf(itemDto.getId())},
    		//		LocaleContextHolder.getLocale());
    		String message = "ID in path does not match ID in object";
    		throw new ValidationException(message);
    	}
    	
    	ConfirmationDto confirmationDto = itemsService.editItem(itemDto);
    	return Response.status(202).entity(confirmationDto).build();
    }
    
   
    /**
     * Delete by id.
     *
     * @param id the id
     * @param response the response
     */
    @Operation(summary = "Deletes an item", description = "Use this API to delete an item.")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "No Content", content = @Content(schema = @Schema(implementation = Void.class))),
            @APIResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @APIResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(implementation = ApiError.class))) })
    //@DeleteMapping(API_PATH + "/{id}")
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteById(@PathParam Long id) {
    	itemsService.deleteItemById(id);
    	Response.noContent().build();
    }
}
