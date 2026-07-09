package fit.reserve.gym;

import fit.reserve.auth.modelo.UserEntity;
import fit.reserve.gym.modelo.GymClass;
import fit.reserve.gym.modelo.Registration;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUser(UserEntity user);

    List<Registration> findByUserAndActiveTrue(UserEntity user);

    boolean existsByUserAndGymClassAndActiveTrue(UserEntity user, GymClass gymClass);
}
