/**
 * @file Privilege.java
 * @brief Entity representing a user privilege in user management
 *
 * This class is an entity that represents a specific privilege the user can have and is used as part of the role-based access control system.
 */
package com.zegline.thubot.core.model.security;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * @class Privilege
 * @brief Entity to represent a specific privilege
 *
 * The Privilege class represents a specific grantable access or action privilege held by a User.
 * These privileges are grouped into Roles and can be used for role-based access control.
*/
@Entity
@Getter
@Setter
public class Privilege {

    /**
     * Unique ID for the privilege.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The name of the privilege.
     */
    @Getter
    private String name;

    /**
     * Collection of roles that have this privilege.
     */
    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    /**
     * Constructor method for a Privilege with a named parameter.
     *
     * @param name The name for the privilege.
     */
    public Privilege(String name) {
        this.name = name;
    }

    /**
     * Default constructor for a Privilege.
     */
    public Privilege() {

    }
}