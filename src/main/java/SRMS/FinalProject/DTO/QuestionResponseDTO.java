package SRMS.FinalProject.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter

public class QuestionResponseDTO {
    private String technology;
    private List<String> questions;
}