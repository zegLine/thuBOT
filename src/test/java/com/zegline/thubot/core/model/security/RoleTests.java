package com.zegline.thubot.core.model.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RoleTests {

    private Role role;

    @BeforeEach
    public void setUp() {
        role = new Role("ROLE_ADMIN");
    }

    @Test
    public void testGetName() {
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    public void testGetPrivileges() {
        Collection<Privilege> privileges = Arrays.asList(
                new Privilege("PRIVILEGE_1"),
                new Privilege("PRIVILEGE_2")
        );
        role.setPrivileges(privileges);

        assertEquals(privileges, role.getPrivileges());
    }

}