package com.zegline.thubot.core.repository;

import org.hibernate.annotations.Parent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.ParentToChild;
public interface ParentToChildRespository extends CrudRepository<ParentToChild, Long>{

}
