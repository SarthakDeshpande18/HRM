package SRMS.FinalProject.DTO;


import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
public class ExperienceDTO {
    private String companyName;
    private String technology;
    private LocalDate startDate;
    private LocalDate endDate;
    private String message;
}