package fit.reserve.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fit.reserve.auth.modelo.UserEntity;
import fit.reserve.auth.repositorio.UserRepository;
import fit.reserve.auth.servicio.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registerShouldPersistUserWithEncodedPassword() {
        UserEntity user = userService.register("test@example.com", "secret123", "USER");

        assertNotNull(user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("USER", user.getRole());
        assertNotEquals("secret123", user.getPassword());
        assertTrue(user.getPassword().startsWith("$2"));
    }

    @Test
    void authenticateShouldSucceedWithRightPassword() {
        userService.register("login@example.com", "pass123", "USER");

        assertTrue(userService.authenticate("login@example.com", "pass123").isPresent());
    }

    @Test
    void authenticateShouldFailWithWrongPassword() {
        userService.register("login@example.com", "pass123", "USER");

        assertTrue(userService.authenticate("login@example.com", "wrong").isEmpty());
    }
}
