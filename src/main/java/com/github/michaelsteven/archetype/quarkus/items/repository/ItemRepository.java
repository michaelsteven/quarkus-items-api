package com.github.michaelsteven.archetype.quarkus.items.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.michaelsteven.archetype.quarkus.items.model.ItemEntity;


/**
 * The Interface ItemRepository.
 */
@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

}
