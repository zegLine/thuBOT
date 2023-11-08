package com.zegline.thubot.core.model;

import jakarta.persistence.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

@Entity
public class ParentToChild {

    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name = "dialogNode_id")
    private DialogNode parent;

    @ManyToOne
    @JoinColumn(name = "dialogNode_id")
    private DialogNode child;

    public DialogNode getParent() {
        return parent;
    }

    public DialogNode getChild() {
        return child;
    }
}
