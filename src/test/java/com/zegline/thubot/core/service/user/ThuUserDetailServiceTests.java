package com.zegline.thubot.core.service.user;

import com.zegline.thubot.core.model.security.Privilege;
import com.zegline.thubot.core.model.security.User;
import com.zegline.thubot.core.repository.UserRepository;
import com.zegline.thubot.core.model.security.Role;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class ThuUserDetailServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ThuUserDetailService userDetailService;

    @Test
    public void testLoadUserByUsername() {
        // Arrange
        String username = "testuser";
        User user = new User();
        Role role = new Role();
        Privilege privilege = new Privilege();
        privilege.setName("PRIVILEGE_USER");
        role.setName("ROLE_USER");
        user.setUsername(username);
        user.setPassword("password");
        user.setRoles(Collections.singleton(role));
        role.setPrivileges(Collections.singleton(privilege));


        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);

        // Act
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        // Assert
        Assert.assertNotNull(userDetails);
        Assert.assertEquals(username, userDetails.getUsername());
        Assert.assertEquals(user.getPassword(), userDetails.getPassword());
        Assert.assertEquals(2, userDetails.getAuthorities().size());
        //System.out.println(userDetails.getAuthorities());
        Assert.assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        UserRepository ur = Mockito.mock(UserRepository.class);

        Mockito.when(ur.findByUsername(username)).thenThrow(new UsernameNotFoundException(username));
        Assert.assertEquals(ur.findByUsername(username), new UsernameNotFoundException(username));

    }
}
