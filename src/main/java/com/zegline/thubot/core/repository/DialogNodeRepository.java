package com.zegline.thubot.core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.DialogNode;

@Repository
public interface DialogNodeRepository extends CrudRepository<DialogNode, String> {
    
}
