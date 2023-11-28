/**
 * @file DialogNodeRepository.java
 * @brief Interface for CRUD operations on DialogNode entities
 *
 * This interface provides access to the DialogNode database, allowing for simple CRUD operations.
 * Additional query methods can be defined for specific search criteria related to DialogNodes
 */
package com.zegline.thubot.core.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.DialogNode;

import java.util.List;

@Repository
public interface DialogNodeRepository extends CrudRepository<DialogNode, String> {

    DialogNode findByChildren(DialogNode child);

    @Query(value = "select d1_0.id from dialog_node d1_0 left join dialog_node_children c1_0 on d1_0.id=c1_0.children_id where c1_0.children_id is null", nativeQuery = true)
    List<String> findIdsWithNoChildren();

    //Todo Test
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

    //Todo Test
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
