package SRMS.FinalProject.Service;



import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.ProfileDTO;
import SRMS.FinalProject.Repository.*;
import SRMS.FinalProject.UserEntity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;


@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private UserTechnologyRepository userTechnologyRepository;

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ApiResponse<String> addProfileFields(Long id, ProfileDTO profileDTO) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with Id " + id));


            user.setBloodGroup(profileDTO.getBloodGroup());
            user.setFirstName(profileDTO.getFirstName());
            user.setMiddleName(profileDTO.getMiddleName());
            user.setLastName(profileDTO.getLastName());


            if (profileDTO.getDOB() != null && !profileDTO.getDOB().isEmpty()) {
                LocalDate dob = LocalDate.parse(profileDTO.getDOB(), INPUT_FORMATTER);
                user.setDOB(dob.format(INPUT_FORMATTER)); // Store as dd-MM-yyyy
            }


            if (profileDTO.getTechId() != null) {
                Technology technology = technologyRepository.findById(profileDTO.getTechId())
                        .orElseThrow(() -> new RuntimeException("Technology not found with Id " + profileDTO.getTechId()));

                // Check if the UserTechnology link already exists to avoid duplicates
                boolean exists = userTechnologyRepository.existsByUserAndTechnology(user, technology);
                if (!exists) {
                    UserTechnology userTechnology = new UserTechnology(user, technology);
                    userTechnologyRepository.save(userTechnology);
                }
            }

            userRepository.save(user); // Save updated user

            // Save Education info
            Education education = new Education();
            education.setUser(user);
            education.setCourse_name(profileDTO.getCourseName());
            education.setInstitute_name(profileDTO.getInstituteName());
            educationRepository.save(education);

            // Save Experience info
            Experience experience = new Experience();
            experience.setUser(user);
            experience.setMessage(profileDTO.getMessage());
            experience.setCompanyName(profileDTO.getCompanyName());

            // Set the technology for the experience
            if (profileDTO.getTechId() != null) {
                Technology technology = technologyRepository.findById(profileDTO.getTechId())
                        .orElseThrow(() -> new RuntimeException("Technology not found with Id " + profileDTO.getTechId()));
                experience.setTechnology(technology);
            }

            // Set start and end dates
            if (profileDTO.getStartDate() != null && !profileDTO.getStartDate().isEmpty()) {
                LocalDate startDate = LocalDate.parse(profileDTO.getStartDate(), INPUT_FORMATTER);
                experience.setStartDate(startDate.format(INPUT_FORMATTER));
            }

            if (profileDTO.getEndDate() != null && !profileDTO.getEndDate().isEmpty()) {
                LocalDate endDate = LocalDate.parse(profileDTO.getEndDate(), INPUT_FORMATTER);
                experience.setEndDate(endDate.format(INPUT_FORMATTER));
            }

            experienceRepository.save(experience); // Save experience

            return new ApiResponse<>(200, "Profile fields stored successfully!", null);

        } catch (DataAccessException ex) {
            return new ApiResponse<>(500, "Database error occurred: " + ex.getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>(500, "An unexpected error occurred: " + ex.getMessage(), null);
        }
    }
}
