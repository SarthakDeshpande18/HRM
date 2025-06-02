package SRMS.FinalProject.UserEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview")
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class InterviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = true)
    private Long interviewerId;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = true)
    private String interviewerEmail;

    @Column(nullable = false)
    private String mode; // Online/Offline

    private String meetingLink; // Only for Online mode

    private String location; // Only for Offline mode

    private String remarks;

    @Column(nullable = false)
    private String status;

    private String candidateEmail;

    // Getters and Setters
    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }
}

