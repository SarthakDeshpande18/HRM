package SRMS.FinalProject.Repository;



import SRMS.FinalProject.UserEntity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>, PagingAndSortingRepository<JobApplication, Long> {
    Page<JobApplication> findAll(Pageable pageable);
    Page<JobApplication> findByUserId(Long userId, Pageable pageable);
    Page<JobApplication> findByStatusIn(List<String> statuses, Pageable pageable);


}