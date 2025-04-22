package SRMS.FinalProject.Repository;

import SRMS.FinalProject.UserEntity.QuestionAnswersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswersEntity, Long> {
    List<QuestionAnswersEntity> findByTechnology(String technology);
    List<QuestionAnswersEntity> findByTechnologyIn(List<String> technologies);

}