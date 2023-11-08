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
    @GeneratedValue(generator="questionid-generator")
    @GenericGenerator(name = "questionid-generator", strategy = "com.zegline.thubot.core.utils.generator.QuestionIdGenerator",
    parameters = { @Parameter(name = "prefix", value = "QN") })

    @Column(name = "id")
    private String id;
    
    @Column(name="dialog_text")
    private String dialogText;

    @Column(name="msg_text")
    private String msgText;

    @OneToMany(mappedBy = "dialogNode")
    Set<DialogNodeToResponse> questionresponse;

    @ManyToOne()
    private DialogNode parent;

    @ManyToMany()
    private Set<DialogNode> children = new HashSet<>();

    public DialogNode() {}

    public DialogNode(String q, String p) {
        dialogText = q;
        msgText = p;
    }

    public String getQuestionText() {
        return dialogText;
    }

    public String toString() {
        return "<Question> " + dialogText;
    }

    public DialogNode getParent() {
        return this.parent;
    }

    public void setParent(DialogNode dn) {
        this.parent = dn;
    }
    
}
