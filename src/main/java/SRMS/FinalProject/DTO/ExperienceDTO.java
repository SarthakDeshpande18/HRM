package SRMS.FinalProject.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class ExperienceDTO {

    @Column(nullable = false)
    private String companyName;
    private String technology;
    @JsonFormat(pattern = "dd-mm-yyy")
    private String startDate;
    @JsonFormat(pattern = "dd-mm-yyy")
    private String endDate;
    private String message;
}
