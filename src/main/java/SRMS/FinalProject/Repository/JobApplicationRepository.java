package SRMS.FinalProject.Repository;



import SRMS.FinalProject.UserEntity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>, PagingAndSortingRepository<JobApplication, Long> {
    Page<JobApplication> findAll(Pageable pageable);
}