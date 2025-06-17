package SRMS.FinalProject.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "questions")
public class QuestionEntity {

    @Id
    private String questionId;

    private String question;

    private String parentQuestionId; // Nullable

    private String answerValue; // What user must answer to reach this question
}
