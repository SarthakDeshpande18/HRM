package SRMS.FinalProject.DTO;

import SRMS.FinalProject.UserEntity.JobApplication;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class JobApplicationResponse {
    private Long id;
    private Long jobId;
    private String jobDescription;
    private Long userId;
    private String interviewerEmail;
    private Long interviewerId;
    private String filePath;
    private LocalDateTime appliedAt;
    private String status;
}
