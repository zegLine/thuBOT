/**
 * @file DialogNodeResponseRepository.java
 * @brief Interface for CRUD operations on DialogNodeToResponse entities
 *
 * This interface allows for creating, reading, updating, and deleting DialogNodeToResponse
 * entities from the database. It extends CrudRepository provided by Spring Data
 */
package com.zegline.thubot.core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.DialogNodeToResponse;

/**
 * @interface DialogNodeResponseRepository
 * @brief Repository interface for DialogNodeToResponse entities
 *
 * By extending CrudRepository, this interface automatically inherits several methods for working
 * with DialogNodeToResponse data, such as saving, deleting, and finding DialogNodeToResponse entities
 */
@Repository
public interface DialogNodeResponseRepository extends CrudRepository<DialogNodeToResponse, Long>{
    
}
