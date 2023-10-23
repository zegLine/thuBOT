package com.zegline.thubot.core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.Response;

@Repository
public interface ResponseRepository extends CrudRepository<Response, Long> {
}
