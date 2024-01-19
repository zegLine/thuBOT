/**
 * @file UserRepository.java
 * @brief Interface for CRUD operations on User entities.
 *
 * This interface extends JpaRepository to provide CRUD operations for User entities. It contains additional methods to find a User by its username and to check if a User with a specified username exists.
 */
package com.zegline.thubot.core.repository;

import com.zegline.thubot.core.model.security.User;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @interface UserRepository
 * @brief Repository interface for User entities providing CRUD operations and additional methods.
 *
 * By extending JpaRepository, this interface inherits several methods for working with User data such as saving, deleting, finding, and paging through User entities. Additionally, it provides a method to find a User by its username and to check if a User with a specified username exists.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User by its username.
     * @param username - The username of the user.
     * @return The User with the supplied username.
     */
    User findByUsername(String username);

    /**
     * Checks if a User with a specified username exists.
     * @param username - The username of the user.
     * @return true if a User with the supplied username exists, false otherwise.
     */
    boolean existsByUsername(String username);
}
