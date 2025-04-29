package SRMS.FinalProject.UserEntity;

import jakarta.persistence.*;

@Entity
@Table(name = "Job_Openings")
public class JobOpenings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jobDescription", nullable = false)
    private String jobDescription;


    public int getYearOfExperience() {
        return yearOfExperience;
    }

    public void setYearOfExperience(int yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }

    @Column(name = "Year_Of_Experience", nullable = false)
    private int yearOfExperience;

    @Column(name = "JobType", nullable = false)
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




    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
}
