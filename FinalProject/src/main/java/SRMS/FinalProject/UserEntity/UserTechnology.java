package SRMS.FinalProject.UserEntity;

import jakarta.persistence.*;
//
@Entity
@Table(name = "user_technology")
public class UserTechnology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "techId", nullable = false)
    private Technology technology;

    // Constructors
    public UserTechnology() {}

    public UserTechnology(User user, Technology technology) {
        this.user = user;
        this.technology = technology;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }
}
