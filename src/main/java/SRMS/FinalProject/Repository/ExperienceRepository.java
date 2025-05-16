package SRMS.FinalProject.Repository;

import SRMS.FinalProject.UserEntity.Experience;
import SRMS.FinalProject.UserEntity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    Optional<Experience> findByUserId(Long userId); // Fetch experiences of a specific user
    void deleteByUser(User user);
}