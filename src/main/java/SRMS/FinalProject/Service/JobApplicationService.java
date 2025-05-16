package SRMS.FinalProject.Service;

import SRMS.FinalProject.DTO.JobApplicationResponse;
import SRMS.FinalProject.Repository.*;
import SRMS.FinalProject.UserEntity.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class JobApplicationService {

    private static final String UPLOAD_DIR = "D:/HRM/src/main/resources/uploads/";

    private final JobApplicationRepository jobApplicationRepository;
    private final JavaMailSender mailSender;
    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserTechnologyRepository userTechnologyRepository;
    private final TechnologyRepository technologyRepository;

    @Autowired
    public JobApplicationService(JobApplicationRepository jobApplicationRepository,
                                 JavaMailSender mailSender,
                                 ExperienceRepository experienceRepository,
                                 UserRepository userRepository,
                                 UserRoleRepository userRoleRepository,
                                 UserTechnologyRepository userTechnologyRepository,
                                 TechnologyRepository technologyRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.mailSender = mailSender;
        this.experienceRepository = experienceRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userTechnologyRepository = userTechnologyRepository;
        this.technologyRepository = technologyRepository;
    }


    public JobApplicationResponse submitApplication(MultipartFile file, Long userId) {
        try {
            // Get user's technology using userId
            String userTechnology = getUserTechnology(userId);

            // Fetch matching interviewers
            Technology technology = technologyRepository.findByTechName(userTechnology)
                    .orElseThrow(() -> new RuntimeException("Technology not found: " + userTechnology));
            List<User> interviewers = userRepository.findInterviewersByTechnology(technology.getTech_id());

            if (interviewers.isEmpty()) {
                throw new RuntimeException("No interviewer found with matching technology: " + userTechnology);
            }

            // Randomly select one interviewer
            User selectedInterviewer = interviewers.get(new Random().nextInt(interviewers.size()));

            // Ensure upload directory exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Save uploaded file
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR).resolve(uniqueFileName).normalize();
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Save job application
            JobApplication application = new JobApplication();
            application.setUserId(userId);  // Use userId directly
            application.setFilePath(filePath.toString());
            application.setAppliedAt(LocalDateTime.now());
            application.setStatus("PENDING");
            application.setInterviewerId(selectedInterviewer.getId());

            // Store the interviewer's email in the application
            String interviewerEmail = getInterviewerEmail(selectedInterviewer.getId());
            application.setInterviewerEmail(interviewerEmail);

            JobApplication savedApplication = jobApplicationRepository.save(application);

            // Send email to selected interviewer
            sendEmailNotification(selectedInterviewer.getId(), filePath.toString(), userId);

            // Create a response object
            JobApplicationResponse response = new JobApplicationResponse();
            response.setId(savedApplication.getId());
            response.setUserId(savedApplication.getUserId());
            response.setInterviewerId(selectedInterviewer.getId());
            response.setInterviewerEmail(interviewerEmail);
            response.setFilePath(savedApplication.getFilePath());
            response.setAppliedAt(savedApplication.getAppliedAt());
            response.setStatus(savedApplication.getStatus());

            // Return the response
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Application submission failed: " + e.getMessage());
        }
    }

    // Send Email Notification with Resume Attachment to a Single Interviewer
    private void sendEmailNotification(Long interviewerId, String filePath, Long userId) {
        try {
            // Fetch the applicant's details
            User applicant = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Applicant not found"));
            String applicantName = applicant.getFirstName() + " " + applicant.getLastName();
            String applicantEmail = applicant.getEmail();
            String applicantTechnology = getUserTechnology(userId);

            // Fetch the interviewer's email
            String interviewerEmail = getInterviewerEmail(interviewerId);

            // Create the email message
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(interviewerEmail);
            helper.setSubject("New Job Application Received");

            // Email content
            helper.setText(
                    "Dear Interviewer,\n\n" +
                            "A new job application has been submitted.\n\n" +
                            "🔹 Applicant ID: " + userId + "\n" +
                            "🔹 Name: " + applicantName + "\n" +
                            "🔹 Email: " + applicantEmail + "\n" +
                            "🔹 Technology: " + applicantTechnology + "\n\n" +
                            "Please find the resume attached.\n\n" +
                            "Regards,\nHRM Portal"
            );

            // Attach the uploaded file
            FileSystemResource fileResource = new FileSystemResource(new File(filePath));
            helper.addAttachment("Resume_" + userId + ".pdf", fileResource);

            // Send the email
            mailSender.send(message);
            System.out.println("Email sent successfully to interviewer: " + interviewerEmail);

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Failed to send email to interviewer");
        }
    }

    private String getUserTechnology(Long userId) {
        UserTechnology userTech = userTechnologyRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User technology not found"));
        return userTech.getTechnology().getTechName();
    }

    public String getInterviewerEmail(Long interviewerId) {
        return userRepository.findById(interviewerId)
                .orElseThrow(() -> new RuntimeException("Interviewer not found"))
                .getEmail();
    }

    public Page<JobApplication> getAllApplications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // Define the page and size for pagination
        return jobApplicationRepository.findAll(pageable); // Fetch paginated data
    }

    public void updateApplicationStatus(Long id, String status) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application id " + id + " not found "));

        // Validate status change
        application.setStatus(status);
        jobApplicationRepository.save(application);
    }
}
