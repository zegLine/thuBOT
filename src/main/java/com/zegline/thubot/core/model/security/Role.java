/**
 * @file Role.java
 * @brief Entity representing a user role in user management
 *
 * This class is an entity that represents a specific role the user can have.
 * Each role combines multiple Privileges.
 */
package com.zegline.thubot.core.model.security;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * @class Role
 * @brief Entity to represent a specific role
 *
 * The Role class represents a specific role that a User can have in the
 * system. Each role is essentially a collection of Privileges, and thus
 * can be used for role-based access control.
 */
@Entity
public class Role {

    /**
     * Unique ID for the role.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The name of the role.
     */
    @Getter
    @Setter
    private String name;

    /**
     * Collection of users who have this role.
     */
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    /**
     * Collection of privileges that this role holds.
     */
    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"
            )
    )
    private Collection<Privilege> privileges;

    /**
     * Constructor method for a Role with a named parameter.
     *
     * @param name The name for the role.
     */
    public Role(String name) {
        this.name = name;
    }

    /**
     * Default constructor for a Role.
     */
    public Role(){

    }

    /**
     * Method to set privileges to the role
     *
     * @param privileges Collection of Privilege entities
     */
    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges = privileges;
    }
}
