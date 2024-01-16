/**
 * @file DialogNodeToResponse.java
 * @brief Entity class that represents the association between dialog nodes and responses
 *
 * This class is a join entity that maps the many-to-many relationship between dialog nodes and responses
 * into two many-to-one relationships
 */
package com.zegline.thubot.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


/**
 * @class DialogNodeToResponse
 * @brief Represents a link between a DialogNode and a Response entity
 *
 * This entity serves as a join table in the database which holds the association between a dialog node
 * and a response. Each DialogNode can have multiple responses associated with it, and each response can be
 * associated with multiple dialog nodes
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

    /**
     * Returns the DialogNode question associated with this DialogNode-Response relationship.
     *
     * @return The DialogNode associated with this relationship.
     */
    public DialogNode getQuestion() {
        return dialogNode;
    }

    /**
     * Returns the Response entity associated with this DialogNode-Response relationship.
     *
     * @return The Response associated with this relationship.
     */
    public Response getResponse() {
        return response;
    }
}
