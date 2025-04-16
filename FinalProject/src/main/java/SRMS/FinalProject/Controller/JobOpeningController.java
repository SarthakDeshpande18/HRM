package SRMS.FinalProject.Controller;

import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.Service.JobOpeningService;
import SRMS.FinalProject.UserEntity.JobOpenings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/job-openings")
public class JobOpeningController {

    @Autowired
    private JobOpeningService jobOpeningsService;

    @PostMapping
    public ResponseEntity<ApiResponse<JobOpenings>> createJobOpening(@RequestBody JobOpenings jobOpening) {
        JobOpenings savedJob = jobOpeningsService.addJobOpening(jobOpening);
        if (savedJob == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(409, "Job opening already exists", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Job opening created successfully", savedJob));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<JobOpenings>>> getAllJobOpenings() {
        List<JobOpenings> jobList = jobOpeningsService.getAllJobOpenings();
        return ResponseEntity.ok(new ApiResponse<>(200, "Fetched all job openings", jobList));
    }

    @PostMapping("/getById")
    public ResponseEntity<ApiResponse<JobOpenings>> getJobOpeningById(@RequestBody Map<String, Long> payload) {
        Long id = payload.get("id");
        return jobOpeningsService.getJobOpeningById(id)
                .map(job -> ResponseEntity.ok(new ApiResponse<>(200, "Successful", job)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(404, "Job Opening ID " + id + " not found", null)));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<JobOpenings>> updateJobOpening(@RequestBody JobOpenings jobOpening) {
        Long id = jobOpening.getId();
        try {
            JobOpenings updated = jobOpeningsService.updateJobOpening(id, jobOpening);
            return ResponseEntity.ok(new ApiResponse<>(200, "Job opening ID " + id + "updated successfully", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Job Opening ID " + id + " not found", null));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteJobOpening(@RequestBody Map<String, Long> payload) {
        Long id = payload.get("id");
        boolean deleted = jobOpeningsService.deleteJobOpening(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Job opening Id " + id + " deleted successfully", null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Job Opening ID " + id + " not found", null));
        }
    }
}