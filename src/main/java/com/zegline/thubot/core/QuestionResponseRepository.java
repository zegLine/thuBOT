package com.zegline.thubot.core;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionResponseRepository extends CrudRepository<QuestionResponse, Long>{
    List<QuestionResponse> findByQuestion(Question q);
}
