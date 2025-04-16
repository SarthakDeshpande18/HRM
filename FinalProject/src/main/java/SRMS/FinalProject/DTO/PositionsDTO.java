package SRMS.FinalProject.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PositionsDTO {

    @NotNull(message = "ID is required for update")
    private Long id;

    @NotBlank(message = "Position name must not be blank")
    private String positionName;

    @NotBlank(message = "Position description must not be blank")
    private String positionDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionDescription() {
        return positionDescription;
    }

    public void setPositionDescription(String positionDescription) {
        this.positionDescription = positionDescription;
    }


}
