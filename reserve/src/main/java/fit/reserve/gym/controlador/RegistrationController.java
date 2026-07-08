package fit.reserve.gym.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fit.reserve.gym.servicio.RegistrationService;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService = new RegistrationService();

    @PostMapping("/registrar")
    public String registrar(@RequestParam Long userId,
                            @RequestParam Long classId,
                            Model model) {

        String mensaje = registrationService.register(userId, classId);
        model.addAttribute("mensaje", mensaje);

        return "index"; // tu vista principal
    }
}
