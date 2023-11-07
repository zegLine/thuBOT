package com.zegline.thubot.core.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.model.DialogNodeResponse;

@Repository
public interface DialogNodeResponseRepository extends CrudRepository<DialogNodeResponse, Long>{
    //List<DialogNodeResponse> findByQuestion(DialogNode q);
}
