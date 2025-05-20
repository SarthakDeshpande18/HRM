package SRMS.FinalProject.Controller;


import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.JobApplicationResponse;
import SRMS.FinalProject.Service.CustomUserDetails;
import SRMS.FinalProject.Service.EmailService;
import SRMS.FinalProject.Service.JobApplicationService;
import SRMS.FinalProject.UserEntity.JobApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/job")
//@PreAuthorize("hasAuthority('USER')")
public class JobApplicationController {

    private final JobApplicationService service;
    private final EmailService emailService;
    private static final String UPLOAD_DIR = "D:/HRM/src/main/resources/uploads/";

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

    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-status")
    public ResponseEntity<ApiResponse<String>> updateStatus(@RequestBody JobApplicationResponse request) {
        try {
            String status = request.getStatus();
            if (!status.equalsIgnoreCase("REVIEWED") &&
                    !status.equalsIgnoreCase("ACCEPTED") &&
                    !status.equalsIgnoreCase("REJECTED")) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(400, "Invalid status. Allowed values are REVIEWED, ACCEPTED, REJECTED.", null));
            }

            service.updateApplicationStatus(request.getId(), status);
            return ResponseEntity.ok(new ApiResponse<>(200, "Status updated successfully!", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(500, "Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            // Prevent path traversal attacks
            if (filename.contains("..")) {
                return ResponseEntity.badRequest().body(null);
            }

            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Dynamically detect content type (fallback to PDF)
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/pdf";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/my-applications")
    public ResponseEntity<ApiResponse<Page<JobApplication>>> getMyApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUserId();

            Page<JobApplication> applications = service.getByUserId(userId, page, size);

            if (applications.isEmpty()) {
                return ResponseEntity.ok(
                        new ApiResponse<>(204, "No applications found", applications)  // 204 No Content, but still returning body
                );
            }

            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Applications fetched successfully!", applications)
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(500, "Error: " + e.getMessage(), null));
        }
    }


    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadResume(@PathVariable String filename) {
        try {
            if (filename.contains("..")) {
                return ResponseEntity.badRequest().build(); // prevent path traversal
            }

            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();

            System.out.println("Downloading file from: " + filePath.toString());

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                System.out.println("File not found or not readable.");
                return ResponseEntity.notFound().build();
            }

            // Determine content type dynamically
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/pdf"; // default to PDF
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"") // 👈 Change here
                    .body(resource);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




}



