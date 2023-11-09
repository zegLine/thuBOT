package com.zegline.thubot.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


/**
 * Entity class representing the relationship between a DialogNode and its associated Response.
 * This entity establishes a link between a DialogNode and a Response through their IDs.
 */
@Entity
public class DialogNodeToResponse {

    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private DialogNode dialogNode;

    @ManyToOne
    @JoinColumn(name = "response_id")
    private Response response;

    public DialogNode getQuestion() {
        return dialogNode;
    }

    public Response getResponse() {
        return response;
    }
}
