package fit.reserve.auth.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import fit.reserve.auth.modelo.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
