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

    @OneToMany()
    private Set<DialogNode> children = new HashSet<>();

    public DialogNode() {
    }

    public DialogNode(String q, String p) {
        dialogText = q;
        msgText = p;
    }

    public DialogNode addChild(DialogNode c) {
        this.children.add(c);
        return this;
    }

    public String toString() {
        return "<Dialog> " + dialogText;
    }

    public String getDialogText() {
        return dialogText;
    }

    public String getMsgText() {
        return this.msgText;
    }

    public Set<DialogNode> getChildren() {
        return this.children;
    }

    public void setDialogText(String dialogText) {
        this.dialogText = dialogText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }
    
}
