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

    /**
     * Retrieves IDs of DialogNodes that have no children.
     * 
     * @return List of IDs corresponding to DialogNodes without children.
     */
    @Query(value = "select d1_0.id from dialog_node d1_0 left join dialog_node_children c1_0 on d1_0.id=c1_0.children_id where c1_0.children_id is null", nativeQuery = true)
    List<String> findIdsWithNoChildren();

    /**
     * Retrieves the dialog text of a DialogNode by its ID.
     * 
     * @param nodeId The ID of the DialogNode.
     * @return The dialog text of the specified DialogNode, or null if not found.
     */
    @Query(value = "SELECT dialog_text FROM dialog_node WHERE id = :nodeId", nativeQuery = true)
    String findDialogTextById(@Param("nodeId") String nodeId);

    @Query(value = "SELECT msg_text FROM dialog_node WHERE id = :nodeId", nativeQuery = true)
    String findMSGTextById(@Param("nodeId") String nodeId);

    /**
     * Finds IDs of leaf DialogNodes within the descendants of the specified parent ID, using recursive CTEs.
     * 
     * @param parentId The ID of the parent DialogNode from which to start the search.
     * @return List of IDs corresponding to leaf DialogNodes in the descendants of the specified parent.
     */
    @Query(value = "WITH RECURSIVE descendant_nodes(id) AS ( " +
            "SELECT id " +
            "FROM dialog_node " +
            "WHERE parent_id = :parentId " +
            "UNION ALL " +
            "SELECT d.id " +
            "FROM dialog_node d " +
            "JOIN descendant_nodes dn ON d.parent_id = dn.id " +
            "), leaf_nodes(id) AS ( " +
            "SELECT id " +
            "FROM descendant_nodes " +
            "LEFT JOIN dialog_node_children c ON descendant_nodes.id = c.children_id " +
            "WHERE c.children_id IS NULL " +
            ") " +
            "SELECT d.id " +
            "FROM leaf_nodes d;", nativeQuery = true)
    List<String> findLeafNodesByParentIdAsDescendants(@Param("parentId") String parentId);

    @Query(value = "WITH RECURSIVE excluded_descendants(id) AS ( " +
            "SELECT id " +
            "FROM dialog_node " +
            "WHERE parent_id = :excludedParentId " +
            "UNION ALL " +
            "SELECT d.id " +
            "FROM dialog_node d " +
            "JOIN excluded_descendants dn ON d.parent_id = dn.id " +
            "), remaining_nodes(id) AS ( " +
            "SELECT id " +
            "FROM dialog_node " +
            "WHERE id NOT IN (SELECT id FROM excluded_descendants) " +
            "), leaf_nodes_except_descendants(id) AS ( " +
            "SELECT id " +
            "FROM remaining_nodes " +
            "LEFT JOIN dialog_node_children c ON remaining_nodes.id = c.children_id " +
            "WHERE c.children_id IS NULL " +
            ") " +
            "SELECT d.id " +
            "FROM leaf_nodes_except_descendants d;", nativeQuery = true)
    List<String> findLeafNodesExceptDescendantsOfParentId(@Param("excludedParentId") String excludedParentId);


}