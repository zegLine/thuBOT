package com.zegline.thubot.core;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class QuestionResponse {
    
    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "response_id")
    private Response response;

    public Question getQuestion() {
        return question;
    }

    public Response getResponse() {
        return response;
    }
}
