package SRMS.FinalProject.UserEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "experience")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "technology_id", nullable = false) // FK to technology table
    private Technology technology;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd-mm-yyy")
    private LocalDate startDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd-mm-yyy")
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String message;
}
