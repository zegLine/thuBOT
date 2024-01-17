/**
 * @file User.java
 * @brief Entity representing a user in the user management framework
 *
 * This class is an entity that represents a user of the system. It associates each user with a username and a password.
 * Each User can have several roles, which are used for role-based Access Control.
 */
package com.zegline.thubot.core.model.security;

import jakarta.persistence.*;

import lombok.Getter;

import java.util.Collection;

/**
 * @class User
 * @brief Entity representing a user
 *
 * The User class represents a specific user of the system.
 * The User's information includes a unique id, username, and password,
 * along with a set of roles that dictate access control for the user.
 *
 */
@Entity
public class User {

    /**
     * Unique identifier for the User.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The User's username. This field is unique and non-null.
     */
    @Column(nullable = false, unique = true)
    @Getter
    private String username;

    /**
     * The User's password.
     */
    @Getter
    private String password;

    /**
     * Collection of the roles associated with the User.
     */
    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"
            )
    )

    private Collection<Role> roles;

    /**
     * Method to set the User's username.
     *
     * @param username The User's username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

     /**
     * Method to set the User's password.
     *
     * @param password The User's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Method to set the roles of the User.
     *
     * @param roles The roles to be associated with the User.
     */
    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

}
