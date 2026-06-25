package fit.reserve.auth;

import fit.reserve.gym.GymClass;
import fit.reserve.gym.GymClassRepository;
import fit.reserve.gym.Registration;
import fit.reserve.gym.RegistrationRepository;
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
