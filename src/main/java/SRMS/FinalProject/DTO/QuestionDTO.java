package SRMS.FinalProject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class QuestionDTO {
    private String questionId;   // new field
    private String question;
    private List<String> options;
    private String answer;

    public QuestionDTO(String questionId, String question, List<String> options, String answer) {
        this.questionId = questionId;
        this.question = question;
        this.options = options;
        this.answer = answer;
    }

    // getters and setters for questionId, question, options, answer
}


