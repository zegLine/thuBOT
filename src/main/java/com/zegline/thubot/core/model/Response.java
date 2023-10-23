package com.zegline.thubot.core.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Response {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(name = "response_text")
    private String response_text;

    @OneToMany(mappedBy = "response")
    Set<QuestionResponse> questionResponses;

    public Response() {}

    public Response(String r) {
        response_text = r;
    }

    public String getResponseText() {
        return response_text;
    }

    public void setResponseText(String rt) {
        response_text = rt;
    }
    
}
