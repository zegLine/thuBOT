/**
 * @file DialogNodeResponseRepository.java
 * @brief Interface for CRUD operations on DialogNodeToResponse entities.
 *
 * This interface extends the CrudRepository to provide CRUD operations for DialogNodeToResponse entities.
 * It includes an additional query method for finding associations by their DialogNode.
 */
package com.zegline.thubot.core.repository;

import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.DialogNodeToResponse;
import com.zegline.thubot.core.model.DialogNode;

import java.util.List;

/**
 * @interface DialogNodeResponseRepository
 * @brief Interface providing CRUD operations and custom queries for DialogNodeToResponse entities.
 */
@Repository
public interface DialogNodeResponseRepository extends CrudRepository<DialogNodeToResponse, Long> {
    
    /**
     * Finds all DialogNodeToResponse associations for a specified DialogNode.
     *
     * @param dialogNode The DialogNode to find associations for.
     * @return List of DialogNodeToResponse instances associated with the specified DialogNode.
     */
    List<DialogNodeToResponse> findByDialogNode(DialogNode dialogNode);
}