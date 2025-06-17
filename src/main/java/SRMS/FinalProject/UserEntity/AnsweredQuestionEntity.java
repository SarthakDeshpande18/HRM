package SRMS.FinalProject.UserEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "answered_questions")
public class AnsweredQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String questionId;

    private Long userId;

    private String question;

    private String answer; // "Yes"

    private LocalDateTime answeredAt;
}