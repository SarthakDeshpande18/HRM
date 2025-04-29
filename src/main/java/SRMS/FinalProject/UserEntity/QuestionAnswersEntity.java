package SRMS.FinalProject.UserEntity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question_answers_entity")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String technology;

    private String question;
    private String answer;
}
