/**
 * @file ThuUserDetailService.java
 * @brief Service class for handling user authentication.
 *
 * This class implements UserDetailsService from the Spring security framework to load user-specific data 
 * and is primarily used by the authentication manager to perform authentication.
 */
package com.zegline.thubot.core.service.user;

import com.zegline.thubot.core.model.security.User;
import com.zegline.thubot.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @class ThuUserDetailService
 * @brief Service class for loading user-specific data needed for authentication.
 *
 * Implements UserDetailsService from the Spring Security framework to provide authentication services. It uses a UserRepository 
 * to retrieve User entities from the database.
 */
@Service
public class ThuUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Loads the user entity by its username.
     *
     * @param username The username identifying the user.
     * @return UserDetails object that Spring Security can use for authentication and validation.
     * @throws UsernameNotFoundException if the user entity was not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new ThuUserPrincipal(user);
    }
}
