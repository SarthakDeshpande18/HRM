package SRMS.FinalProject.Service;
import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.Repository.InterviewRepository;
import SRMS.FinalProject.UserEntity.InterviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InterviewService {
    private final InterviewRepository repository;
    private final EmailService emailService;

    public InterviewService(InterviewRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public ResponseEntity<ApiResponse<InterviewEntity>> scheduleInterview(InterviewEntity interview) {
        if (!isValidEmail(interview.getCandidateEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "Email is not in proper format", null));
        }

        boolean emailExists = repository.existsByCandidateEmail(interview.getCandidateEmail());
        if (emailExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(409, "Email is already used", null));
        }

        boolean isInterviewerBooked = repository.existsByInterviewerIdAndDateTime(interview.getInterviewerId(), interview.getDateTime());
        if (isInterviewerBooked) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(409, "Interviewer is already scheduled for another interview at this time.", null));
        }

        InterviewEntity savedInterview = repository.save(interview);
        emailService.sendEmail(
                interview.getCandidateEmail(),
                "Interview Scheduled",
                "Dear Candidate,\nYour interview has been scheduled.\nDate & Time: " + interview.getDateTime() +
                        "\nMode: " + interview.getMode() +
                        (interview.getMode().equals("Online") ? "\nMeeting Link: " + interview.getMeetingLink() : "\nLocation: " + interview.getLocation()) +
                        "\n\nBest Regards,\nHR Team"
        );

        ApiResponse<InterviewEntity> response = new ApiResponse<>(201, "Interview scheduled successfully", savedInterview);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public ResponseEntity<ApiResponse<Optional<InterviewEntity>>> getInterviewById(Long id) {
        Optional<InterviewEntity> interview = repository.findById(id);
        ApiResponse<Optional<InterviewEntity>> response = interview.isPresent()
                ? new ApiResponse<>(200, "Interview found", interview)
                : new ApiResponse<>(404, "Interview not found", Optional.empty());
        return ResponseEntity.status(interview.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }

    public ResponseEntity<ApiResponse<Page<InterviewEntity>>> getAllInterviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateTime").descending());
        Page<InterviewEntity> interviews = repository.findAll(pageable);
        ApiResponse<Page<InterviewEntity>> response = new ApiResponse<>(200, "Interviews retrieved successfully", interviews);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<ApiResponse<InterviewEntity>> updateInterview(Long id, InterviewEntity updatedInterview) {
        return repository.findById(id).map(interview -> {
            interview.setDateTime(updatedInterview.getDateTime());
            interview.setMode(updatedInterview.getMode());
            interview.setMeetingLink(updatedInterview.getMeetingLink());
            interview.setLocation(updatedInterview.getLocation());
            interview.setRemarks(updatedInterview.getRemarks());
            interview.setStatus(updatedInterview.getStatus());

            InterviewEntity savedInterview = repository.save(interview);
            return ResponseEntity.ok(new ApiResponse<>(200, "Interview updated successfully", savedInterview));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(404, "Interview not found", null)));
    }

    public ResponseEntity<ApiResponse<String>> cancelInterview(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            ApiResponse<String> response = new ApiResponse<>(200, "Interview canceled successfully", null);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<String> response = new ApiResponse<>(404, "Interview not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
