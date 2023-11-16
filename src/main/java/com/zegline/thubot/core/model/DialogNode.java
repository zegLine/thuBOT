/**
 * @file DialogNode.java
 * @brief Entity representing a dialog node in a conversation flow
 *
 * This class is used to model a node in a conversational dialog flow, where each node represents a point in the conversation.
 * Nodes have a hierarchical structure with parent and child relationships
 */

package com.zegline.thubot.core.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 * @class DialogNode
 * @brief Represents a node within a dialog conversation
 *
 * A dialog node is an entity that contains text for both the dialog (question) and the message (response).
 * It is part of a larger conversation flow and can have relationships to other nodes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class DialogNode {
    @Getter
    @Id
    @GeneratedValue(generator = "questionid-generator")
    @GenericGenerator(name = "questionid-generator", strategy = "com.zegline.thubot.core.utils.generator.QuestionIdGenerator", parameters = {
            @Parameter(name = "prefix", value = "QN") })

    @Column(name = "id")
    private String id;

    @Column(name = "dialog_text")
    private String dialogText;

    @Getter
    @Column(name = "msg_text")
    private String msgText;

    @OneToMany(mappedBy = "dialogNode")
    Set<DialogNodeToResponse> questionresponse;

    @Getter
    @OneToMany()
    private Set<DialogNode> children = new HashSet<>();

    /**
     * Constructor for DialogNode.
     * @param q <b>String</b> The text the dialog node will contain.
     * @param p <b>String</b> The message printed out after the DialogNode's text.
     */
    public DialogNode(String q, String p) {
        dialogText = q;
        msgText = p;
    }

    /**
     * Adds a child to the current DialogNode.
     * @param c <b>DialogNode</b> Child to be added to the children list of the DialogNode.
     * @return <i>this</i> <d>DialogNode</d>
     */
    public DialogNode addChild(DialogNode c) {
        this.children.add(c);
        return this;
    }

    /**
     * Converts DialogNode to a String
     * @return <b>String</b> The Text that the node contains
     */
    public String toString() {
        return "<Dialog> " + dialogText;
    }


    public void setDialogText(String dialogText) {
        this.dialogText = dialogText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }
    
}
