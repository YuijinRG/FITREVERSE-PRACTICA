package fit.reserve.gym.servicio;

import org.springframework.stereotype.Service;

import fit.reserve.gym.RegistrationRepository;
import fit.reserve.gym.modelo.GymClass;
import fit.reserve.gym.modelo.Registration;
import fit.reserve.gym.repositorio.GymClassRepository;

@Service
public class RegistrationService {

    private final GymClassRepository gymClassRepository = null;

    private final RegistrationRepository registrationRepository = null;

    public String register(Long userId, Long classId) {

        // Buscar la clase
        GymClass gymClass = gymClassRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        // Validar cupos
        if (gymClass.getCapacity() <= 0) {
            return "No hay cupos disponibles";
        }

        // Restar cupo
        gymClass.setCapacity(gymClass.getCapacity() - 1);
        gymClassRepository.save(gymClass);

        // Registrar la reserva
        Registration reg = new Registration();
        reg.setUserId(userId);
        reg.setClassId(classId);
        registrationRepository.save(reg);

        return "Registro exitoso";
    }
}
