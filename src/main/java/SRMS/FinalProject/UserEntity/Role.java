package SRMS.FinalProject.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
//
//@Entity
//@Table(name = "roles")
//public class Role {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "role_id")
//    private Long roleId;
//
//    @Column(unique = true, nullable = false)
//    private String roleName;
//
//    @Column(nullable = true)
//    private String description;
//
//    @ManyToMany(mappedBy = "roles")
//    @JsonIgnore
//    private Set<User> users = new HashSet<>();
//
//    // Constructors
//    public Role() {}
//
//    public Role(String roleName, String description) {
//        this.roleName = roleName;
//        this.description = description;
//    }
//
//    // Getters and Setters
//    public Long getRoleId() { return roleId; }
//    public void setRoleId(Long roleId) { this.roleId = roleId; }
//
//    public String getRoleName() { return roleName; }
//    public void setRoleName(String roleName) { this.roleName = roleName; }
//
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//
//    public Set<User> getUsers() { return users; }
//    public void setUsers(Set<User> users) { this.users = users; }
//}

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(unique = true, nullable = false)
    private String roleName;

    @Column(nullable = true)
    private String description;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore // Optional: Add this if you don't want the `users` relationship to be serialized
    private Set<User> users = new HashSet<>();

    // Constructors
    public Role() {}

    public Role(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    // Getters and Setters
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }
}
