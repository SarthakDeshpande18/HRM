package SRMS.FinalProject.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTechnologyDTO {
    private Long id;
    private String techName;
    private Long userId;
    private Long technologyId;

    public UserTechnologyDTO(Long id, String techName,Long userId, Long technologyId) {
        this.id=id;
        this.techName=techName;
        this.userId = userId;
        this.technologyId = technologyId;
    }
}