/**
 * @file ThuUserPrincipal.java
 * @brief User principal class handling the security details of user entity.
 *
 * This class implements UserDetails interface from Spring Security and 
 * wraps around a User entity to serve as the input of authentication provider.
 */
package com.zegline.thubot.core.service.user;

import com.zegline.thubot.core.model.security.Privilege;
import com.zegline.thubot.core.model.security.Role;
import com.zegline.thubot.core.model.security.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @class ThuUserPrincipal
 * @brief Implements UserDetails interface for User authentication.
 *
 * Wraps around a User entity and provides details needed for authentication.
 */
public class ThuUserPrincipal implements UserDetails {

    private User user;

    /**
     * Constructor that accepts a User entity.
     *
     * @param user The User entity to be used for authentication.
     */
    public ThuUserPrincipal(User user) {
        this.user = user;
    }

    /**
     * Returns the authorities granted to the user.
     * Need not be overridden unless for more complex role and privilege setups.
     *
     * @return A collection of GrantedAuthority which represents the user's authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities(getPrivileges(user.getRoles()));
    }

    /**
     * Obtains all the privileges from the collection of roles associated with the user.
     *
     * @param roles A collection of Role entities linked with the user.
     * @return A list of privileges as Strings.
     */
    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    /**
     * Creates a list of GrantedAuthorities from the list of privileges.
     *
     * @param privileges A list of privileges as Strings.
     * @return A list of GrantedAuthority created from the privileges.
     */
    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
    
    /**
     * Returns the password associated with the user.
     *
     * @return A string representing the user's password.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username of the user.
     *
     * @return A string representing the user's username.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Checks if the user's account has not expired.
     *
     * @return always true in this implementation.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks if the user's account is not locked.
     *
     * @return always true in this implementation.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Checks if the user's credentials (password) has not expired.
     *
     * @return always true in this implementation.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks if the user's account is enabled.
     *
     * @return always true in this implementation.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
