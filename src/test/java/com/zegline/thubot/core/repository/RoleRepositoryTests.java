package com.zegline.thubot.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.zegline.thubot.core.model.security.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByName() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        roleRepository.save(role);
        Role foundRole = roleRepository.findByName("ROLE_ADMIN");
        assertNotNull(foundRole);
        assertEquals("ROLE_ADMIN", foundRole.getName());
    }
}
