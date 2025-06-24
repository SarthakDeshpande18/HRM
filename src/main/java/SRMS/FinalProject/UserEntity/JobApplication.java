package SRMS.FinalProject.UserEntity;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private String filePath;

    private LocalDateTime appliedAt = LocalDateTime.now();

    @NotNull
    private Long jobId;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "interviewer_email")
    private String interviewerEmail;

    @Column(name = "job_description")
    private String jobDescription;

    //Changes From Kunal
    @ElementCollection
    @CollectionTable(name = "application_available_dates", joinColumns = @JoinColumn(name = "application_id"))
    @Column(name = "available_date")
    private List<LocalDate> availableDates;

    @ElementCollection
    @CollectionTable(name = "application_time_slots", joinColumns = @JoinColumn(name = "application_id"))
    @Column(name = "time_slot")
    private List<String> availableTimeSlots;

}