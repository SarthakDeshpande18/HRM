package SRMS.FinalProject.Repository;


import SRMS.FinalProject.UserEntity.JobOpenings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOpeningRepository extends JpaRepository<JobOpenings, Long> {
    boolean existsByJobDescription(String jobDescription);
    boolean existsByJobType(String jobType);
    boolean existsByJobDescriptionAndYearOfExperienceAndJobType(String jobDescription, int yearOfExperience, String jobType);

}