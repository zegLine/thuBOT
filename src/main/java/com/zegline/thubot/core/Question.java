package com.zegline.thubot.core;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

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
