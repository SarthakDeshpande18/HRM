package SRMS.FinalProject.Repository;


import SRMS.FinalProject.UserEntity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionEntity, String> {
    List<QuestionEntity> findByParentQuestionIdIsNull();

    QuestionEntity findByParentQuestionIdAndAnswerValue(String parentId, String answerValue);
}
