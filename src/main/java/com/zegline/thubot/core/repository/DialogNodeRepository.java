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
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.DialogNode;

import java.util.List;

@Repository
public interface DialogNodeRepository extends CrudRepository<DialogNode, String> {
    
   
    List<DialogNode> findByParentIdAndDialogTextContainingIgnoreCase(String parentId, String searchText);
    

    @Query("SELECT dn.dialogText FROM DialogNode dn WHERE dn.parent.id = :parentId")
    List<String> findAllDialogTextByParentId(@Param("parentId") String parentId);
    
    
    DialogNode findByDialogText(String dialogText);

    @Query("SELECT dn.id FROM DialogNode dn WHERE dn.parent IS NULL")
    List<String> findIdsWithNoChildren();

    List<DialogNode> findAllByParentId(String parentId);
}
