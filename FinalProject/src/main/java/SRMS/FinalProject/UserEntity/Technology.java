package SRMS.FinalProject.UserEntity;

import jakarta.persistence.*;

@Entity
@Table(name="technology")
public class Technology
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tech_id;

    @Column(name = "tech_name", unique = true, nullable = false)
    private String techName;

    public String getTechDescription() {
        return techDescription;
    }

    public void setTechDescription(String techDescription) {
        this.techDescription = techDescription;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    @Column(name = "tech_description", unique = true, nullable = false)
    private String techDescription;

    public Long getTech_id() {
        return tech_id;
    }

    public void setTech_id(Long tech_id) {
        this.tech_id = tech_id;
    }

}
