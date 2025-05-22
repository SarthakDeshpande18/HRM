//
package SRMS.FinalProject.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProfileDTO {

    private List<Long> techIds;
    private String technology;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private String DOB;

    private String bloodGroup;

    // Education fields as lists
    private List<String> courseNames;
    private List<String> instituteNames;

    private List<String> passingYearMonth;

    // Experience fields as lists
    @Column(nullable = false)
    private List<String> companyNames;
    private List<String> startDates;  // Dates in "dd-MM-yyyy" string format
    private List<String> endDates;
    private List<String> messages;

    private String firstName;
    private String middleName;
    private String lastName;

    private Long roleId;
}
