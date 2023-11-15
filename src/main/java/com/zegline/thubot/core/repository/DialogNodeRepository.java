package com.zegline.thubot.core.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.DialogNode;

import java.util.List;

@Repository
public interface DialogNodeRepository extends CrudRepository<DialogNode, String> {

    DialogNode findByChildren(DialogNode child);

    @Query(value = "select d1_0.id from dialog_node d1_0 left join dialog_node_children c1_0 on d1_0.id=c1_0.children_id where c1_0.children_id is null", nativeQuery = true)
    List<String> findIdsWithNoChildren();

}
