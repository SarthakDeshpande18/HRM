package SRMS.FinalProject.Controller;


import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.JobApplicationResponse;
import SRMS.FinalProject.Service.CustomUserDetails;
import SRMS.FinalProject.Service.EmailService;
import SRMS.FinalProject.Service.JobApplicationService;
import SRMS.FinalProject.UserEntity.JobApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/job")
//@PreAuthorize("hasAuthority('USER')")
public class JobApplicationController {

    private final JobApplicationService service;
    private final EmailService emailService;

    @Autowired
    public JobApplicationController(JobApplicationService service, EmailService emailService) {
        this.service = service;
        this.emailService = emailService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> submitApplication(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUserId();

            boolean hasUserRole = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("USER"));

            if (!hasUserRole) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(403, "Id "+ userId + "  " +"Role "+ userDetails.getAuthorities()+ "  " +" You do not have the 'USER' role to submit an application.", null));
            }
            System.out.println("Authenticated User ID: " + userId);
            System.out.println("Authorities: " + userDetails.getAuthorities());

            if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(400, "Only PDF files are allowed!", null));
            }

            JobApplicationResponse savedApplication = service.submitApplication(file, userId);

            return ResponseEntity.ok(new ApiResponse<>(200,
                    "Application submitted successfully!", savedApplication));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(500, "Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<JobApplication>>> getAllApplications(
            @RequestParam(defaultValue = "0") int page, // Default is page 0
            @RequestParam(defaultValue = "10") int size) { // Default size is 10
        try {
            Page<JobApplication> applications = service.getAllApplications(page, size);
            return ResponseEntity.ok(new ApiResponse<>(200, "Applications fetched successfully!", applications));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(500, "Error: " + e.getMessage(), null));
        }
    }


    // Endpoint to update application status (pending to reviewed/accepted/rejected)
        @PutMapping("/update-status/{id}")
        public ResponseEntity<ApiResponse<String>> updateStatus(@PathVariable Long id,
                                                                @RequestParam("status") String status) {
            try {
                if (!status.equalsIgnoreCase("REVIEWED") &&
                        !status.equalsIgnoreCase("ACCEPTED") &&
                        !status.equalsIgnoreCase("REJECTED")) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>(400, "Invalid status. Allowed values are REVIEWED, ACCEPTED, REJECTED.", null));
                }

                service.updateApplicationStatus(id, status);
                return ResponseEntity.ok(new ApiResponse<>(200, "Status updated successfully!", null));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(new ApiResponse<>(500, "Error: " + e.getMessage(), null));
            }
        }
}

