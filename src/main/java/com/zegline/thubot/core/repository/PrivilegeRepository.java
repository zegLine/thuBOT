/**
 * @file PrivilegeRepository.java
 * @brief Interface for CRUD operations on Privilege entities.
 *
 * This interface extends the JpaRepository to provide CRUD operations for Privilege entities.
 * It includes an additional query method for finding privileges by name.
 */
package com.zegline.thubot.core.repository;

import com.zegline.thubot.core.model.security.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @interface PrivilegeRepository
 * @brief Interface providing CRUD operations and custom queries for Privilege entities.
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    /**
     * Finds a Privilege by its name.
     * 
     * @param name The name of the Privilege to find.
     * @return The Privilege instance with the specified name.
     */
    Privilege findByName(String name);
}
