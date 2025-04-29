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

            // Set basic fields
            if (profileDTO.getFirstName() != null && !profileDTO.getFirstName().isEmpty()) {
                user.setFirstName(profileDTO.getFirstName());
            }
            if (profileDTO.getMiddleName() != null && !profileDTO.getMiddleName().isEmpty()) {
                user.setMiddleName(profileDTO.getMiddleName());
            }
            if (profileDTO.getLastName() != null && !profileDTO.getLastName().isEmpty()) {
                user.setLastName(profileDTO.getLastName());
            }
            if (profileDTO.getDOB() != null && !profileDTO.getDOB().isEmpty()) {
                LocalDate dob = LocalDate.parse(profileDTO.getDOB(), INPUT_FORMATTER);
                user.setDOB(dob.format(INPUT_FORMATTER));
            }
            if (profileDTO.getBloodGroup() != null && !profileDTO.getBloodGroup().isEmpty()) {
                user.setBloodGroup(profileDTO.getBloodGroup());
            }

            // Technology add
            if (profileDTO.getTechId() != null) {
                Technology technology = technologyRepository.findById(profileDTO.getTechId())
                        .orElseThrow(() -> new RuntimeException("Technology not found with Id " + profileDTO.getTechId()));
                boolean exists = userTechnologyRepository.existsByUserAndTechnology(user, technology);
                if (!exists) {
                    UserTechnology userTechnology = new UserTechnology(user, technology);
                    userTechnologyRepository.save(userTechnology);
                }
            }

            userRepository.save(user); // Save updated user

            // Save Education
            if (profileDTO.getCourseName() != null && profileDTO.getInstituteName() != null) {
                for (int i = 0; i < profileDTO.getCourseName().size(); i++) {
                    Education education = new Education();
                    education.setUser(user);
                    education.setCourse_name(profileDTO.getCourseName().get(i));
                    education.setInstitute_name(profileDTO.getInstituteName().get(i));
                    educationRepository.save(education);
                }
            }

            // Save Experience
            if (profileDTO.getCompanyName() != null && profileDTO.getStartDate() != null &&
                    profileDTO.getEndDate() != null && profileDTO.getMessage() != null) {

                for (int i = 0; i < profileDTO.getCompanyName().size(); i++) {
                    Experience experience = new Experience();
                    experience.setUser(user);
                    experience.setCompanyName(profileDTO.getCompanyName().get(i));
                    experience.setStartDate(profileDTO.getStartDate().get(i));
                    experience.setEndDate(profileDTO.getEndDate().get(i));
                    experience.setMessage(profileDTO.getMessage().get(i));
//
//                    // Ensure that Technology is assigned if techId is provided
//                    if (profileDTO.getTechId() != null) {
//                        Technology technology = technologyRepository.findById(profileDTO.getTechId())
//                                .orElseThrow(() -> new RuntimeException("Technology not found with Id " + profileDTO.getTechId()));
//                        experience.setTechnology(technology); // Set technology to experience
//                    }

                    experienceRepository.save(experience); // Save experience record
                }
            }

            return new ApiResponse<>(200, "Profile fields stored successfully!", null);

        } catch (DataAccessException ex) {
            return new ApiResponse<>(500, "Database error occurred: " + ex.getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>(500, "An unexpected error occurred: " + ex.getMessage(), null);
        }
    }
}
