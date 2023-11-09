package com.zegline.thubot.core.model;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class DialogNode {

    @Id
    @GeneratedValue(generator = "questionid-generator")
    @GenericGenerator(name = "questionid-generator", strategy = "com.zegline.thubot.core.utils.generator.QuestionIdGenerator", parameters = {
            @Parameter(name = "prefix", value = "QN") })

    @Column(name = "id")
    private String id;

    @Column(name = "dialog_text")
    private String dialogText;

    @Column(name = "msg_text")
    private String msgText;

    @OneToMany(mappedBy = "dialogNode")
    Set<DialogNodeToResponse> questionresponse;

    @ManyToOne()
    private DialogNode parent;

    @ManyToMany()
    private Set<DialogNode> children = new HashSet<>();

    public DialogNode() {
    }

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

    public String getDialogText() {
        return dialogText;
    }

    public DialogNode getParent() {
        return this.parent;
    }

    public String getMsgText() {
        return this.msgText;
    }

    public Set<DialogNode> getChildren() {
        return this.children;
    }

    public void setParent(DialogNode dn) {
        this.parent = dn;
    }

    public void setDialogText(String dialogText) {
        this.dialogText = dialogText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }
    
}
