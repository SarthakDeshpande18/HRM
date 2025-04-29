package SRMS.FinalProject.UserEntity;

import jakarta.persistence.*;

@Entity
@Table(name="positions")
public class Positions
{
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionDescription() {
        return positionDescription;
    }

    public void setPositionDescription(String positionDescription) {
        this.positionDescription = positionDescription;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position_name", unique = true, nullable = false)
    private String positionName;

    @Column(name = "position_description", unique = true, nullable = false)
    private String positionDescription;
}
