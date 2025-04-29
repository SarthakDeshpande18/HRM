package SRMS.FinalProject.DTO;

import SRMS.FinalProject.UserEntity.JobApplication;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class JobApplicationResponse {
    private Long id;
    private Long userId;
    private String interviewerEmail;
    private Long interviewerId;
    private String filePath;
    private LocalDateTime appliedAt;
    private String status;
    // Add this field

    // Getters and Setters for all fields, including interviewerId
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInterviewerEmail() {
        return interviewerEmail;
    }

    public void setInterviewerEmail(String interviewerEmail) {
        this.interviewerEmail = interviewerEmail;
    }

    public Long getInterviewerId() {
        return interviewerId;
    }

    public void setInterviewerId(Long interviewerId) {
        this.interviewerId = interviewerId;
    }
}
