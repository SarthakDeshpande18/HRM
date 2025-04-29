package SRMS.FinalProject.DTO;

import java.util.List;

//
//public class UserDto {
//
//    private Long id;
//    private String email;
//    private String username;
//    private String mobileNo;
//    private Boolean emailVerified;
//    private String bloodGroup;
//    private String DOB;
//    private String firstName;
//    private String middleName;
//    private String lastName;
//
//    // Constructor
//    public UserDto(Long id, String email, String username, String mobileNo,
//                   Boolean emailVerified, String bloodGroup, String DOB,
//                   String firstName, String middleName, String lastName) {
//        this.id = id;
//        this.email = email;
//        this.username = username;
//        this.mobileNo = mobileNo;
//        this.emailVerified = emailVerified;
//        this.bloodGroup = bloodGroup;
//        this.DOB = DOB;
//        this.firstName = firstName;
//        this.middleName = middleName;
//        this.lastName = lastName;
//    }
//
//    // Getters and setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//
//    public String getUsername() { return username; }
//    public void setUsername(String username) { this.username = username; }
//
//    public String getMobileNo() { return mobileNo; }
//    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }
//
//    public Boolean getEmailVerified() { return emailVerified; }
//    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }
//
//    public String getBloodGroup() { return bloodGroup; }
//    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
//
//    public String getDOB() { return DOB; }
//    public void setDOB(String DOB) { this.DOB = DOB; }
//
//    public String getFirstName() { return firstName; }
//    public void setFirstName(String firstName) { this.firstName = firstName; }
//
//    public String getMiddleName() { return middleName; }
//    public void setMiddleName(String middleName) { this.middleName = middleName; }
//
//    public String getLastName() { return lastName; }
//    public void setLastName(String lastName) { this.lastName = lastName; }
//}
//
public class UserDto {

    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String username;
    private String mobileNo;
    private String DOB;
    private Boolean emailVerified;
    private String bloodGroup;
    private List<String> roles;
    private List<String> technologies;

    // Constructor for scenarios where roles and technologies are needed
    public UserDto(Long id, String firstName, String middleName, String lastName, String email,
                   String username, String mobileNo, String DOB, Boolean emailVerified,
                   String bloodGroup, List<String> roles, List<String> technologies) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.mobileNo = mobileNo;
        this.DOB = DOB;
        this.emailVerified = emailVerified;
        this.bloodGroup = bloodGroup;
        this.roles = roles;
        this.technologies = technologies;
    }

    // Constructor for scenarios where roles and technologies are not needed
    public UserDto(Long id, String email, String username, String mobileNo, Boolean emailVerified,
                   String bloodGroup, String DOB, String firstName, String middleName, String lastName) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.mobileNo = mobileNo;
        this.emailVerified = emailVerified;
        this.bloodGroup = bloodGroup;
        this.DOB = DOB;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }

    public String getDOB() { return DOB; }
    public void setDOB(String DOB) { this.DOB = DOB; }

    public Boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public List<String> getTechnologies() { return technologies; }
    public void setTechnologies(List<String> technologies) { this.technologies = technologies; }
}
