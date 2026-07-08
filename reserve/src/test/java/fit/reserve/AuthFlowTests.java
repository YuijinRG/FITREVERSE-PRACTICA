package fit.reserve;

import fit.reserve.auth.modelo.UserEntity;
import fit.reserve.auth.servicio.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthFlowTests {

    @Autowired
    private UserService userService;

    @Test
    void registerAndAuthenticateUser() {
        UserEntity created = userService.register("ana@example.com", "123456", "USER");

        assertNotNull(created.getId());
        assertEquals("USER", created.getRole());
        assertNotEquals("123456", created.getPassword());
        assertTrue(created.getPassword().startsWith("$2"));
        assertTrue(userService.authenticate("ana@example.com", "123456").isPresent());
        assertTrue(userService.authenticate("ana@example.com", "wrong-password").isEmpty());
    }

    @Test
    void registerAdminWithAdminRole() {
        UserEntity created = userService.register("admin@example.com", "admin123", "ADMIN");

        assertEquals("ADMIN", created.getRole());
        assertTrue(userService.authenticate("admin@example.com", "admin123").isPresent());
    }
}
