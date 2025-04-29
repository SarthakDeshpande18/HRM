package SRMS.FinalProject.Repository;

import SRMS.FinalProject.UserEntity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
}