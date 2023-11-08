package com.zegline.thubot.core.model;

import java.util.Set;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

    @OneToMany(mappedBy = "dialogNode")
    Set<DialogNodeToResponse> questionresponse;

    public DialogNode() {}

    public DialogNode(String q) {
        dialogText = q;
    }

    public String getQuestionText() {
        return dialogText;
    }

    public String toString() {
        return "<Question> " + dialogText;
    }
    
}
