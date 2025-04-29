
package SRMS.FinalProject.DTO;

import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.Collections;
import java.util.List;

public class ProfileDTO {
    private Long techId;
    private String technology;
    private String DOB;
    private String bloodGroup;

    private List<String> courseName;
    private List<String> instituteName;
    private List<String> companyName;
    private List<String> startDate;
    private List<String> endDate;
    private List<String> message;

    private String firstName;
    private String middleName;
    private String lastName;

    // Getters and Setters

    public Long getTechId() {
        return techId;
    }

    public void setTechId(Long techId) {
        this.techId = techId;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

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

    public List<String> getCourseName() {
        return courseName;
    }

    @JsonSetter("courseName")
    public void setCourseName(Object courseName) {
        this.courseName = convertToList(courseName);
    }

    public List<String> getInstituteName() {
        return instituteName;
    }

    @JsonSetter("instituteName")
    public void setInstituteName(Object instituteName) {
        this.instituteName = convertToList(instituteName);
    }

    public List<String> getCompanyName() {
        return companyName;
    }

    @JsonSetter("companyName")
    public void setCompanyName(Object companyName) {
        this.companyName = convertToList(companyName);
    }

    public List<String> getStartDate() {
        return startDate;
    }

    @JsonSetter("startDate")
    public void setStartDate(Object startDate) {
        this.startDate = convertToList(startDate);
    }

    public List<String> getEndDate() {
        return endDate;
    }

    @JsonSetter("endDate")
    public void setEndDate(Object endDate) {
        this.endDate = convertToList(endDate);
    }

    public List<String> getMessage() {
        return message;
    }

    @JsonSetter("message")
    public void setMessage(Object message) {
        this.message = convertToList(message);
    }

    // Helper method
    private List<String> convertToList(Object value) {
        if (value instanceof String) {
            return Collections.singletonList((String) value);
        } else if (value instanceof List) {
            return (List<String>) value;
        }
        return null;
    }
}
