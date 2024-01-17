/**
 * @file RoleRepository.java
 * @brief Interface for CRUD operations on Role entities.
 *
 * This interface extends JpaRepository to provide CRUD operations for Role entities.
 * It includes additional method to find Role by its name.
 */
package com.zegline.thubot.core.repository;

import com.zegline.thubot.core.model.security.Role;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @interface RoleRepository
 * @brief Repository interface providing CRUD operations and a method to find Role by name.
 *
 * By extending JpaRepository, this interface automatically inherits several methods for
 * working with Role data, such as saving, deleting, finding, and paging through Role entities.
 * Additionally, this interface provides a function to find a Role entity by its name.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
