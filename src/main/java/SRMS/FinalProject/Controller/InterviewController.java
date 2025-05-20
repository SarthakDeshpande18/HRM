package SRMS.FinalProject.Controller;

import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.Service.InterviewService;
import SRMS.FinalProject.UserEntity.InterviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<InterviewEntity>>> getInterview(@PathVariable Long id) {
        return service.getInterviewById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<InterviewEntity>>> getAllInterviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getAllInterviews(page, size);
    }

    @PutMapping("/reschedule/{id}")
    public ResponseEntity<ApiResponse<InterviewEntity>> updateInterview(@PathVariable Long id, @RequestBody InterviewEntity interview) {
        return service.updateInterview(id, interview);
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse<String>> cancelInterview(@PathVariable Long id) {
        return service.cancelInterview(id);
    }
}
