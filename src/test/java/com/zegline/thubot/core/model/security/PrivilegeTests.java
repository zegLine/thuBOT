package com.zegline.thubot.core.model.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PrivilegeTests {
    private Privilege privilege;

    @BeforeEach
    public void setUp() {
        privilege = new Privilege("TEST_PRIVILEGE");
    }

    @Test
    public void testGetName() {
        assertEquals("TEST_PRIVILEGE", privilege.getName());
    }

    @Test
    public void testGetId() {
        assertNull(privilege.getId());
    }

    @Test
    public void testSetName() {
        privilege.setName("NEW_PRIVILEGE");
        assertEquals("NEW_PRIVILEGE", privilege.getName());
    }

    @Test
    public void testSetRoles() {
        Role role1 = new Role("ROLE1");
        Role role2 = new Role("ROLE2");

        privilege.setRoles(Arrays.asList(role1, role2));

        assertNotNull(privilege.getRoles());
        assertEquals(2, privilege.getRoles().size());
        assertTrue(privilege.getRoles().contains(role1));
        assertTrue(privilege.getRoles().contains(role2));
    }
}