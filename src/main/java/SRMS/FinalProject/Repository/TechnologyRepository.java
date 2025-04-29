package SRMS.FinalProject.Repository;

import SRMS.FinalProject.UserEntity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    boolean existsByTechName(String techName);
    Optional<Technology> findByTechName(String techName);
}