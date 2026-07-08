package fit.reserve.gym.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import fit.reserve.gym.modelo.GymClass;


public interface GymClassRepository extends JpaRepository<GymClass, Long> {
}
