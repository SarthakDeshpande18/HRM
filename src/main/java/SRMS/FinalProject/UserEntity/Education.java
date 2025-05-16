package SRMS.FinalProject.UserEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "education")
public class Education
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "passing_year_month", length = 7)
    private String passingYearMonth;
    private String course_name;
    private String Institute_name;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
