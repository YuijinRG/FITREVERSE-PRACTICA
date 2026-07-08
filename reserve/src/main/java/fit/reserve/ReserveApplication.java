package fit.reserve;

import fit.reserve.gym.modelo.GymClass;
import fit.reserve.gym.repositorio.GymClassRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ReserveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReserveApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedGymClasses(GymClassRepository gymClassRepository) {
        return args -> {
            if (gymClassRepository.count() == 0) {
                gymClassRepository.saveAll(List.of(
                        new GymClass("HIIT", "Lunes 8:00 - 9:00", 15),
                        new GymClass("Yoga", "Martes 10:00 - 11:00", 20),
                        new GymClass("Spinning", "Miércoles 18:00 - 19:00", 18),
                        new GymClass("Pilates", "Jueves 16:00 - 17:00", 16)
                ));
            }
        };
    }
}
