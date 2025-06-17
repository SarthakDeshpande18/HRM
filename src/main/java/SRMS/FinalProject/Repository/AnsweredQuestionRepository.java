package SRMS.FinalProject.Repository;

import SRMS.FinalProject.UserEntity.AnsweredQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnsweredQuestionRepository extends JpaRepository<AnsweredQuestionEntity, Long> {
    List<AnsweredQuestionEntity> findByUserId(Long userId);
}
