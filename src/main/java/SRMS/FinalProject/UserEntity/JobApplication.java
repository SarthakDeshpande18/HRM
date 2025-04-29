package SRMS.FinalProject.UserEntity;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "job_applications")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long interviewerId;

    @NotNull
    private String filePath;

    private LocalDateTime interviewDate;
    private LocalDateTime appliedAt = LocalDateTime.now();
    //private boolean isReviewed = false;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "interviewer_email") // Add this column to store interviewer's email
    private String interviewerEmail;

    // Getters and Setters
}