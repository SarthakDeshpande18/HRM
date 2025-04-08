package SRMS.FinalProject.DTO;


public class UserDto {

    private Long id;
    private String email;
    private String username;
    private String mobileNo;
    private Boolean emailVerified;
    private Integer age;
    private String bloodGroup;
    //    private String education;
//    private String experience;
    private String technology;
    private String DOB;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String firstName;
    private String middleName;
    private String lastName;

    // Constructor
    public UserDto(Long id, String email, String username, String mobileNo,
                   Boolean emailVerified, String bloodGroup, String DOB, String firstName, String middleName, String lastName) {
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


    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }
}
