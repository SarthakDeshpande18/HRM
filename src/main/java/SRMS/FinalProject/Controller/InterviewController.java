package SRMS.FinalProject.Controller;

import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.IdRequest;
import SRMS.FinalProject.Service.CustomUserDetails;
import SRMS.FinalProject.Service.InterviewService;
import SRMS.FinalProject.UserEntity.InterviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {
    private final InterviewService service;

    public InterviewController(InterviewService service) {
        this.service = service;
    }

    @PostMapping("/schedule")
    public ResponseEntity<ApiResponse<InterviewEntity>> scheduleInterview(@RequestBody InterviewEntity interview) {
        return service.scheduleInterview(interview);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<InterviewEntity>>> getAllInterviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getAllInterviews(page, size);
    }

    @PostMapping("/GetById")
    public ResponseEntity<ApiResponse<Optional<InterviewEntity>>> getInterview(@RequestBody IdRequest request) {
        return service.getInterviewById(request.getId());
    }

    @PutMapping("/reschedule")
    public ResponseEntity<ApiResponse<InterviewEntity>> updateInterview(@RequestBody InterviewEntity interview) {
        return service.updateInterview(interview.getId(), interview);
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<ApiResponse<String>> cancelInterview(@RequestBody IdRequest request) {
        return service.cancelInterview(request.getId());
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<InterviewEntity>>> getMyInterviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUserId();

            Page<InterviewEntity> interviews = service.getInterviewsByUserId(userId, page, size);

            if (interviews.isEmpty()) {
                return ResponseEntity.ok(
                        new ApiResponse<>(204, "No interviews found", interviews)
                );
            }

            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Interviews fetched successfully!", interviews)
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(500, "Error: " + e.getMessage(), null));
        }
    }
}
