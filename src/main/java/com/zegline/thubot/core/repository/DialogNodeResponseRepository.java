package com.zegline.thubot.core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.DialogNodeToResponse;

@Repository
public interface DialogNodeResponseRepository extends CrudRepository<DialogNodeToResponse, Long>{
    
}
