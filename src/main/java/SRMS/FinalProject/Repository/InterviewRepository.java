package SRMS.FinalProject.Repository;
import  SRMS.FinalProject.UserEntity.InterviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface InterviewRepository extends JpaRepository<InterviewEntity, Long> {
    boolean existsByInterviewerIdAndDateTime(Long interviewerId, LocalDateTime dateTime);
    boolean existsByCandidateEmail(String candidateEmail);
    Page<InterviewEntity> findByUserId(Long userId,Pageable pageable);

}


