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

    @Column(name = "Year_Of_Experience", nullable = false)
    private String yearOfExperience;

    @Column(name = "JobType", nullable = false)
    private String jobType;

    @ManyToOne(fetch = FetchType.LAZY) // or EAGER, depending on your use case
    @JoinColumn(name = "tech_id")
    private Technology technology;

    public String getYearOfExperience() {
        return yearOfExperience;
    }

    public void setYearOfExperience(String yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }


    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

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
