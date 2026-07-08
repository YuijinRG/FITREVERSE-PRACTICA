package fit.reserve.auth.servicio;

import fit.reserve.auth.modelo.UserEntity;
import fit.reserve.auth.repositorio.UserRepository;
import fit.reserve.gym.RegistrationRepository;
import fit.reserve.gym.modelo.GymClass;
import fit.reserve.gym.modelo.Registration;
import fit.reserve.gym.repositorio.GymClassRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GymClassRepository gymClassRepository;
    private final RegistrationRepository registrationRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository,
                       GymClassRepository gymClassRepository,
                       RegistrationRepository registrationRepository) {
        this.userRepository = userRepository;
        this.gymClassRepository = gymClassRepository;
        this.registrationRepository = registrationRepository;
    }

    public UserEntity register(String email, String password, String role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role.toUpperCase());
        return userRepository.save(user);
    }

    public Optional<UserEntity> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public List<GymClass> getAvailableClasses() {
        return gymClassRepository.findAll();
    }

    public Registration registerForClass(UserEntity user, Long classId) {
        UserEntity managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        GymClass gymClass = gymClassRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));

        if (registrationRepository.existsByUserAndGymClass(managedUser, gymClass)) {
            throw new IllegalArgumentException("Ya estás registrado en esta clase");
        }

        Registration registration = new Registration(managedUser, gymClass);
        return registrationRepository.save(registration);
    }

    public List<Registration> getRegistrationsForUser(UserEntity user) {
        return registrationRepository.findByUser(user);
    }

    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }
}
