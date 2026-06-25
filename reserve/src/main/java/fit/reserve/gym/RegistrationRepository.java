package fit.reserve.gym;

import fit.reserve.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUser(UserEntity user);
    boolean existsByUserAndGymClass(UserEntity user, GymClass gymClass);
}
