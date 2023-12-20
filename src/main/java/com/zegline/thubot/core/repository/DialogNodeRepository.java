/**
 * @file DialogNodeRepository.java
 * @brief Interface for CRUD operations on DialogNode entities.
 *
 * This interface extends the CrudRepository to provide CRUD operations for DialogNode entities.
 * It includes additional query methods for specific search criteria related to DialogNodes, such as
 * fetching nodes without children or finding leaf nodes within the dialog tree structure.
 */
package com.zegline.thubot.core.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.DialogNode;

import java.util.List;

/**
 * Repository interface for DialogNode entities, providing CRUD operations and custom queries.
 */
@Repository
public interface DialogNodeRepository extends CrudRepository<DialogNode, String> {

    /**
     * Finds a DialogNode that has the specified child.
     * 
     * @param child The DialogNode to search for in the children relation.
     * @return The parent DialogNode of the specified child, or null if no parent is found.
     */
    DialogNode findByChildren(DialogNode child);

    List<DialogNode> findDialogNodesByParentIsNull();

}