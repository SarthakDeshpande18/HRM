package SRMS.FinalProject.DTO;

import SRMS.FinalProject.UserEntity.QuestionAnswersEntity;
import lombok.Data;

import java.util.List;

@Data
public class QuestionAnswersRequestDTO {
    private String filter; // "getAll", "getById", "add", "deleteById", "updateById", "getByTechnology"
    private Long id;
    private String technology;
    private List<QuestionAnswersEntity> questions;
    private QuestionAnswersEntity question;
}
