package SRMS.FinalProject.UserEntity;

import jakarta.persistence.*;

@Entity
@Table(name = "Job_Openings")
public class JobOpenings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jobDescription", unique = true, nullable = false)
    private String jobDescription;

    @Column(name = "Year_Of_Experience", nullable = false)
    private String Year_Of_Experience;

    @Column(name = "JobType", unique = true, nullable = false)
    private String jobType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getYear_Of_Experience() {
        return Year_Of_Experience;
    }

    public void setYear_Of_Experience(String year_Of_Experience) {
        this.Year_Of_Experience = year_Of_Experience;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
}
