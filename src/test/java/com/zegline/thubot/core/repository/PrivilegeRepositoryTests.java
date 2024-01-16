package com.zegline.thubot.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.zegline.thubot.core.model.security.Privilege;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PrivilegeRepositoryTests {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Test
    public void testFindByName() {
        // Create a privilege
        Privilege privilege = new Privilege();
        privilege.setName("ADMIN");
        privilegeRepository.save(privilege);

        // Find the privilege by name
        Privilege foundPrivilege = privilegeRepository.findByName("ADMIN");

        // Assert that the privilege is not null
        assertNotNull(foundPrivilege);

        // Assert that the found privilege has the correct name
        assertEquals("ADMIN", foundPrivilege.getName());
    }
}
