/**
 * @file ResponseRepository.java
 * @brief Interface for CRUD operations on Response entities
 *
 * This interface provides an abstraction layer for CRUD operations on Response entities,
 * allowing the application to interact with the data source without having to deal with
 * database-specific details
 */
package com.zegline.thubot.core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.Response;

/**
 * @interface ResponseRepository
 * @brief Repository interface for Response entities
 *
 * Extends CrudRepository to provide basic CRUD operations for Response entities. It includes
 * additional functionality to search for responses containing a specified text
 */
@Repository
public interface ResponseRepository extends CrudRepository<Response, Long> {
}
