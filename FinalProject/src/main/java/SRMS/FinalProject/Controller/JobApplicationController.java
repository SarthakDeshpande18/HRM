package SRMS.FinalProject.Controller;


import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.Service.EmailService;
import SRMS.FinalProject.Service.JobApplicationService;
import SRMS.FinalProject.UserEntity.JobApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/job")
public class JobApplicationController {

    private final JobApplicationService service;
    private final EmailService emailService;  // Add EmailService

    @Autowired
    public JobApplicationController(JobApplicationService service, EmailService emailService) {
        this.service = service;
        this.emailService = emailService;
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<JobApplication>> submitApplication(
            @RequestParam("userId") Long userId,
            @RequestParam("interviewerId") Long interviewerId,
            @RequestParam("interviewDate") LocalDateTime interviewDate,
            @RequestParam("file") MultipartFile file) {

        try {
            // Validate file type (Only PDFs allowed)
            if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(400, "Only PDF files are allowed!", null));
            }

            // Validate interview date (should be in the future)
            if (interviewDate.isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(400, "Interview date must be in the future!", null));
            }

            // Call service method & get saved application
            JobApplication savedApplication = service.submitApplication(userId, interviewerId, interviewDate, file);

            // Fetch interviewer's email for response
            String interviewerEmail = service.getInterviewerEmail(interviewerId); // Now accessible since public

            // Send email to the interviewer
            String emailBody = "Dear Interviewer, \n\n" +
                    "You have a new job application for the interview scheduled on: " + interviewDate.toString() + "\n\n" +
                    "Best regards,\nYour Job Application Team";

            emailService.sendEmail(interviewerEmail, "New Job Application Submitted", emailBody);

            return ResponseEntity.ok(new ApiResponse<>(200,
                    "Application submitted successfully! Email sent to interviewer: " + interviewerEmail,
                    savedApplication));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(500, "Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<JobApplication>>> getAllApplications() {
        try {
            List<JobApplication> applications = service.getAllApplications();
            return ResponseEntity.ok(new ApiResponse<>(200, "Applications fetched successfully!", applications));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(500, "Error: " + e.getMessage(), null));
        }
    }

    @PutMapping("/update-status/{applicationId}")
    public ResponseEntity<ApiResponse<String>> updateStatus(@PathVariable Long applicationId, @RequestParam("status") String status) {
        try {
            service.updateApplicationStatus(applicationId, status);
            return ResponseEntity.ok(new ApiResponse<>(200, "Status updated successfully!", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(500, "Error: " + e.getMessage(), null));
        }
    }
}