/**
 * @file Response.java
 * @brief Entity class for storing response text associated with dialog nodes
 *
 * This class represents a response entity in the database which stores the response text. Each response
 * can be associated with multiple dialog nodes through a DialogNodeToResponse relationship.
 */
package com.zegline.thubot.core.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * @class Response
 * @brief Entity representing a response in the system.
 *
 * The Response entity contains the response text that can be associated with multiple dialog nodes. The associations
 * are maintained through a set of DialogNodeToResponse entities which map each response to various dialog nodes.
 */
@Entity
public class Response {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(name = "response_text")
    private String response_text;

    @OneToMany(mappedBy = "response")
    Set<DialogNodeToResponse> dialogNodeRespons;

    /**
     * Default Constructor for Response
     */
    public Response() {}

    /**
     * Constructor for Response with response text initialization.
     *
     * @param r The response text to initialize.
     */
    public Response(String r) {
        response_text = r;
    }

    /**
     * Returns the response text.
     *
     * @return The text of the response.
     */
    public String getResponseText() {
        return response_text;
    }

    /**
     * Updates the response text.
     *
     * @param rt The text to set as the response.
     */
    public void setResponseText(String rt) {
        response_text = rt;
    }
    
}
