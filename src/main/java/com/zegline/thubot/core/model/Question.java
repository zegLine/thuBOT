package com.zegline.thubot.core.model;

import java.util.Set;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Question {

    @Id
    @GeneratedValue(generator="questionid-generator")
    @GenericGenerator(name = "questionid-generator", strategy = "com.zegline.thubot.core.utils.generator.QuestionIdGenerator",
    parameters = { @Parameter(name = "prefix", value = "QN") })
    @Column(name = "id")
    private String id;

    @Column(name = "question_text")
    private String question_text;

    @OneToMany(mappedBy = "question")
    Set<QuestionResponse> questionresponses;

    public Question() {}

    public Question(String q) {
        question_text = q;
    }

    public String getQuestionText() {
        return question_text;
    }

    public String toString() {
        return "<Question> " + question_text;
    }
    
}
