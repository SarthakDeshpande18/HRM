package SRMS.FinalProject.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AnswerDTO {
    private String previousQuestion;
    private String answer; // Yes / No / Custom
}

