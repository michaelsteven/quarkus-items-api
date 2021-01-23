package com.github.michaelsteven.archetype.quarkus.items.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new item entity.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "items")
public class ItemEntity {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	@Column(name = "created_ts")
	private Instant createdTimestamp;
}
