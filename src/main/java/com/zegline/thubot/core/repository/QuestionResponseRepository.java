package com.zegline.thubot.core.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zegline.thubot.core.model.Question;
import com.zegline.thubot.core.model.QuestionResponse;

@Repository
public interface QuestionResponseRepository extends CrudRepository<QuestionResponse, Long>{
    List<QuestionResponse> findByQuestion(Question q);
}
