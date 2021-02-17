package com.github.michaelsteven.archetype.quarkus.items.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.michaelsteven.archetype.quarkus.items.interceptor.TraceLog;
import com.github.michaelsteven.archetype.quarkus.items.model.ConfirmationDto;
import com.github.michaelsteven.archetype.quarkus.items.model.ItemDto;
import com.github.michaelsteven.archetype.quarkus.items.model.ItemEntity;
import com.github.michaelsteven.archetype.quarkus.items.model.ItemStatus;
import com.github.michaelsteven.archetype.quarkus.items.model.event.Compliance;
import com.github.michaelsteven.archetype.quarkus.items.model.event.ComplianceAction;
import com.github.michaelsteven.archetype.quarkus.items.repository.ItemRepository;

/**
 * The Class ItemsServiceImpl.
 */
@TraceLog
@ApplicationScoped
public class ItemsServiceImpl implements ItemsService {
		
	public static final Logger logger = LoggerFactory.getLogger(ItemsServiceImpl.class);
	
	@Inject
	ItemRepository itemRepository;
	
	private MessageSource messageSource;
	
	
	
	
	/**
	 * Gets the items.
	 *
	 * @param pageable the pageable
	 * @return the items
	 */
	@Override
	@Compliance(action = ComplianceAction.read)
	public Page<ItemDto> getItems(Pageable pageable){
		Page<ItemEntity> page = itemRepository.findAll(pageable);
		return page.map(obj -> convert(obj));
	}
	
	
	/**
	 * Gets the item by id.
	 *
	 * @param id the id
	 * @return the item by id
	 */
	@Override
	@Compliance(action = ComplianceAction.read)
	public Optional<ItemDto> getItemById(long id){
		Optional<ItemEntity> optionalEntity = itemRepository.findById(id);
		return optionalEntity.map(entity -> Optional.of(convert(entity))).orElse(Optional.empty());
	}
	
	
	/**
	 * Save item.
	 *
	 * @param itemDto the item dto
	 * @return the confirmation dto
	 */
	@Override
	@Compliance(action = ComplianceAction.create)
	public ConfirmationDto saveItem(@NotNull @Valid ItemDto itemDto) {
		ItemEntity itemEntity = convert(itemDto);
		ItemEntity savedEntity = itemRepository.save(itemEntity);
		return createConfirmationDto(ItemStatus.SUBMITTED, savedEntity);
	}
	
	
	/**
	 * Edits the item.
	 *
	 * @param itemDto the item dto
	 * @return the confirmation dto
	 */
	@Override
	@Transactional
	@Compliance(action = ComplianceAction.update)
	public ConfirmationDto editItem(@NotNull @Valid ItemDto itemDto) {
		ItemEntity originalEntity = itemRepository.findById(itemDto.getId())
				.orElseThrow(() -> new ValidationException(
						messageSource.getMessage("itemsservice.validationexception.entitynotfoundforid", 
								new Object[] { String.valueOf(itemDto.getId()) },
								//LocaleContextHolder.getLocale()
								Locale.US
								)
						)
					);
		convert(itemDto, originalEntity);
		ItemEntity savedItem = itemRepository.save(originalEntity);
		return createConfirmationDto(ItemStatus.SUBMITTED, savedItem);
	}
	
	
	/**
	 * Delete item by id.
	 *
	 * @param id the id
	 */
	@Override
	@Compliance(action = ComplianceAction.delete)
	public void deleteItemById(long id){
		itemRepository.findById(id).ifPresent(entity -> itemRepository.delete(entity));
	}
	
	
	/**
	 * Creates the confirmation dto.
	 *
	 * @param itemStatus the item status
	 * @param entity the entity
	 * @return the confirmation dto
	 */
	private ConfirmationDto createConfirmationDto(ItemStatus itemStatus, ItemEntity entity) {
		ConfirmationDto confirmationDto = new ConfirmationDto();
		confirmationDto.setStatus(itemStatus);
		if(null != entity) {
			confirmationDto.setId(entity.getId());
			if(null != entity.getCreatedTimestamp()) {
				ZonedDateTime dateSubmitted = ZonedDateTime.ofInstant(entity.getCreatedTimestamp(), ZoneOffset.UTC);
				confirmationDto.setDateSubmitted(dateSubmitted);
			}
		}
		else {
			logger.warn("itemEntity is null");
		}
		return confirmationDto;
	}
	
	/**
	 * Convert.
	 *
	 * @param sourceDto the source dto
	 * @return the item entity
	 */
	private ItemEntity convert(ItemDto sourceDto) {
		ItemEntity entity = new ItemEntity();
		convert(sourceDto, entity);
		return entity;
	}
	
	/**
	 * Convert.
	 *
	 * @param sourceDto the source dto
	 * @param targetEntity the target entity
	 */
	private void convert(@NotNull ItemDto sourceDto, ItemEntity targetEntity) {
		if(null != targetEntity) {
			targetEntity.setId(sourceDto.getId());
			targetEntity.setName(sourceDto.getName());
			targetEntity.setDescription(sourceDto.getDescription());
		}
	}
	
	/**
	 * Convert.
	 *
	 * @param sourceEntity the source entity
	 * @return the item dto
	 */
	private ItemDto convert(ItemEntity sourceEntity) {
		if(null == sourceEntity) {
			return null;
		}
		ZonedDateTime dateSubmitted = null;
		
		if(null != sourceEntity.getCreatedTimestamp()) {
			dateSubmitted = ZonedDateTime.ofInstant(sourceEntity.getCreatedTimestamp(), ZoneOffset.UTC);
		}
		return new ItemDto(sourceEntity.getId(), sourceEntity.getName(), sourceEntity.getDescription(), dateSubmitted);
	}
}
