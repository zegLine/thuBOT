/**
 * @file ResponseRepository.java
 * @brief Interface for CRUD operations on Response entities.
 *
 * This interface extends the CrudRepository to provide CRUD operations for Response entities.
 */
package com.zegline.thubot.core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.Response;

/**
 * @interface ResponseRepository
 * @brief Repository interface providing CRUD operations for Response entities.
 *
 * By extending CrudRepository, this interface automatically inherits several methods for
 * working with Response data, such as saving, deleting, and finding Response entities.
 */
@Repository
public interface ResponseRepository extends CrudRepository<Response, Long> {
}
