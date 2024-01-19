package com.zegline.thubot.core.model.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collection;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserTests {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void setUsername_ValidUsername_Success() {
        String username = "mike.ox.long";
        user.setUsername(username);
        Assertions.assertEquals(username, user.getUsername());
    }

    @Test
    public void setPassword_ValidPassword_Success() {
        String password = "bofaDeezNuts";
        user.setPassword(password);
        Assertions.assertEquals(password, user.getPassword());
    }

    @Test
    public void setRoles_ValidRoles_Success() {
        Role role1 = new Role("ROLE_ADMIN");
        Role role2 = new Role("ROLE_USER");
        Collection<Role> roles = Arrays.asList(role1, role2);
        user.setRoles(roles);
        Assertions.assertEquals(roles, user.getRoles());
    }
}