/**
 * @file DialogNodeRepository.java
 * @brief Interface for CRUD operations on DialogNode entities.
 *
 * This interface extends the CrudRepository to provide CRUD operations for DialogNode entities.
 * It includes additional query methods for specific search criteria related to DialogNodes, such as
 * fetching nodes without children or finding leaf nodes within the dialog tree structure.
 */
package com.zegline.thubot.core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.DialogNode;

import java.util.List;

/**
 * @interface DialogNodeRepository
 * @brief Interface providing CRUD operations and custom queries for DialogNode entities.
 */
@Repository
public interface DialogNodeRepository extends CrudRepository<DialogNode, String> {

    /**
     * Finds a parent DialogNode of the specified child node.
     * 
     * @param child The DialogNode to search for in the parent's children.
     * @return The parent DialogNode of the specified child, or null if no parent is found.
     */
    DialogNode findByChildren(DialogNode child);

    /**
    * Finds all DialogNodes which have no parent node (root nodes).
    *
    * @return List of DialogNode instances without a parent node.
    */
    List<DialogNode> findDialogNodesByParentIsNull();

}