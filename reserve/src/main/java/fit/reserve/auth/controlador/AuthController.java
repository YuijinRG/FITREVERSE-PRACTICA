package fit.reserve.auth.controlador;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fit.reserve.auth.modelo.UserEntity;
import fit.reserve.auth.servicio.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String loginPage() {
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        return userService.authenticate(email, password)
                .map(user -> {
                    session.setAttribute("user", user);
                    if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                        return "redirect:/admin";
                    }
                    return "redirect:/usuario";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Credenciales inválidas");
                    return "index";
                });
    }

    @GetMapping("/registro")
    public String registroPage() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String role,
                           Model model) {
        try {
            userService.register(email, password, role);
            model.addAttribute("success", "Registro correcto. Ahora puedes iniciar sesión.");
            return "index";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "registro";
        }
    }

    @GetMapping("/admin")
    public String adminPage(HttpSession session, Model model) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        model.addAttribute("registrations", userService.getAllRegistrations());
        return "admin";
    }

    @GetMapping("/usuario")
    public String usuarioPage(HttpSession session, Model model) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        model.addAttribute("classes", userService.getAvailableClasses());
        model.addAttribute("registrations", userService.getRegistrationsForUser(user));
        return "usuario";
    }

    @PostMapping("/reservar")
    public String reservarClase(@RequestParam Long classId,
                                HttpSession session,
                                Model model) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        try {
            userService.registerForClass(user, classId);
            model.addAttribute("success", "Te has registrado correctamente a la clase.");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        model.addAttribute("user", user);
        model.addAttribute("classes", userService.getAvailableClasses());
        model.addAttribute("registrations", userService.getRegistrationsForUser(user));
        return "usuario";
    }
}
