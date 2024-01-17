package com.zegline.thubot.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zegline.thubot.core.model.security.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByUsername() {
        // Arrange
        User user = new User();
        user.setUsername("mike.cox.oft");
        entityManager.persist(user);
        entityManager.flush();

        // Act
        User foundUser = userRepository.findByUsername("mike.cox.oft");

        // Assert
        assertNotNull(foundUser);
        assertEquals("mike.cox.oft", foundUser.getUsername());
    }

    @Test
    public void testExistsByUsername() {
        // Arrange
        User user = new User();
        user.setUsername("mike.cox.long");
        entityManager.persist(user);
        entityManager.flush();

        // Act
        boolean exists = userRepository.existsByUsername("mike.cox.long");

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testExistsByUsername_NotFound() {
        // Act
        boolean exists = userRepository.existsByUsername("nonexistent");

        // Assert
        assertFalse(exists);
    }
}
