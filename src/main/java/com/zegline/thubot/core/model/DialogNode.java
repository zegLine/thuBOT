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
 
 import com.fasterxml.jackson.annotation.*;
 import jakarta.persistence.*;
 
 import lombok.AllArgsConstructor;
 import lombok.Builder;
 import lombok.Data;
 import lombok.NoArgsConstructor;
 import lombok.Getter;
 
 import org.hibernate.annotations.GenericGenerator;
 import org.hibernate.annotations.Parameter;
 
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
 
     @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
     @JsonIdentityReference(alwaysAsId = true)
     @ManyToOne
     @JoinColumn(name = "parent_id")
     private DialogNode parent;
 
     @Getter
     @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL) // This binds the 'children' collection to the 'parent' of each child entity
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
         c.setParent(this);
         return this;
     }
 
     /**
      * Adds multiple children to the current DialogNode.
      *
      * @param nodes A set of children DialogNodes to be added to the children list of the DialogNode.
      * @return The updated DialogNode with the new children.
      */
     public DialogNode addChildren(Set<DialogNode> nodes) {
         for (DialogNode n : nodes) {
             this.children.add(n);
             n.setParent(this);
         }
         return this;
     }
 
     /**
      * Returns a string representation of the DialogNode.
      *
      * @return String The dialog text that the node contains enclosed in "<Dialog> ".
      */
     public String toString() {
         return "<Dialog> " + dialogText;
     }
 
     /**
      * Sets the text for the DialogNode
      * @param dialogText The text to be set
      */
     public void setDialogText(String dialogText) {
         this.dialogText = dialogText;
     }
 
     /**
      * Sets the message text for the DialogNode
      * @param msgText The message text to be set
      */
     public void setMsgText(String msgText) {
         this.msgText = msgText;
     }
 
     /**
      * Checks if the current DialogNode is equal to another DialogNode
      * @param obj Object to be compared for equality
      * @return boolean representing the result of the equality check
      */
     @Override
     public boolean equals(Object obj) {

         if (this == obj)
             return true;

         if (obj == null || getClass() != obj.getClass())
             return false;
             
         DialogNode other = (DialogNode) obj;
         return id != null && id.equals(other.getId());
     }

     /**
      * Calculates and returns the hash code for the DialogNode
      * @return int representing the hash code of the DialogNode
      */
     @Override
     public int hashCode() {
         return getClass().hashCode();
     }
 }
