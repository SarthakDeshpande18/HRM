package SRMS.FinalProject.Service;



import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.ProfileDTO;
import SRMS.FinalProject.Repository.*;
import SRMS.FinalProject.UserEntity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.time.format.DateTimeFormatter;
//
//@Service
//public class ProfileService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private EducationRepository educationRepository;
//
//    @Autowired
//    private ExperienceRepository experienceRepository;
//
//    @Autowired
//    private TechnologyRepository technologyRepository;
//
//    @Autowired
//    private UserTechnologyRepository userTechnologyRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Transactional
//    public ApiResponse<String> addProfileFields(Long id, ProfileDTO profileDTO, MultipartFile profileImage) {
//        try {
//            User user = userRepository.findById(id)
//                    .orElseThrow(() -> new RuntimeException("User not found with Id " + id));
//
//            // Update basic user fields
//            if (profileDTO.getFirstName() != null && !profileDTO.getFirstName().isEmpty()) {
//                user.setFirstName(profileDTO.getFirstName());
//            }
//            if (profileDTO.getMiddleName() != null && !profileDTO.getMiddleName().isEmpty()) {
//                user.setMiddleName(profileDTO.getMiddleName());
//            }
//            if (profileDTO.getLastName() != null && !profileDTO.getLastName().isEmpty()) {
//                user.setLastName(profileDTO.getLastName());
//            }
//
//            // Parse and set DOB
//            if (profileDTO.getDOB() != null && !profileDTO.getDOB().isEmpty()) {
//                LocalDate dob = LocalDate.parse(profileDTO.getDOB(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//                user.setDOB(dob);
//            }
//
//            // Set blood group
//            if (profileDTO.getBloodGroup() != null && !profileDTO.getBloodGroup().isEmpty()) {
//                user.setBloodGroup(profileDTO.getBloodGroup());
//            }
//
//            // Save profile image if provided
//            if (profileImage != null && !profileImage.isEmpty()) {
//                String fileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
//                String uploadDir = "D:/HRM/uploads/profile-pictures/";
//
//
//                File uploadPath = new File(uploadDir);
//                if (!uploadPath.exists()) uploadPath.mkdirs();
//
//                File destinationFile = new File(uploadPath, fileName);
//                profileImage.transferTo(destinationFile);
//
//                // Save relative path or URL
//                user.setProfilePicture("/" + uploadDir + fileName);
//            }
//
//            // Update user's technologies
//            if (profileDTO.getTechIds() != null && !profileDTO.getTechIds().isEmpty()) {
//                // Delete old user technologies first
//                userTechnologyRepository.deleteByUser(user);
//
//                for (Long techId : profileDTO.getTechIds()) {
//                    Technology technology = technologyRepository.findById(techId)
//                            .orElseThrow(() -> new RuntimeException("Technology not found with Id " + techId));
//                    UserTechnology userTechnology = new UserTechnology(user, technology);
//                    userTechnologyRepository.save(userTechnology);
//                }
//            }
//
//            // Save user after all updates
//            userRepository.save(user);
//
//            // === EDUCATION ===
//            if (profileDTO.getCourseNames() != null && profileDTO.getInstituteNames() != null && profileDTO.getPassingYearMonth() != null) {
//                int size = Math.min(Math.min(profileDTO.getCourseNames().size(),
//                                profileDTO.getInstituteNames().size()),
//                        profileDTO.getPassingYearMonth().size());
//
//                for (int i = 0; i < size; i++) {
//                    Education education = new Education();
//                    education.setUser(user);
//                    education.setCourse_name(profileDTO.getCourseNames().get(i));
//                    education.setInstitute_name(profileDTO.getInstituteNames().get(i));
//                    education.setPassingYearMonth(profileDTO.getPassingYearMonth().get(i));
//                    educationRepository.save(education);
//                }
//            }
//
//            // === EXPERIENCE ===
//            if (profileDTO.getCompanyNames() != null && profileDTO.getStartDates() != null &&
//                    profileDTO.getEndDates() != null && profileDTO.getMessages() != null) {
//
//                int size = Math.min(Math.min(profileDTO.getCompanyNames().size(),
//                                profileDTO.getStartDates().size()),
//                        Math.min(profileDTO.getEndDates().size(),
//                                profileDTO.getMessages().size()));
//
//                for (int i = 0; i < size; i++) {
//                    Experience experience = new Experience();
//                    experience.setUser(user);
//                    experience.setCompanyName(profileDTO.getCompanyNames().get(i));
//
//                    LocalDate startDate = LocalDate.parse(profileDTO.getStartDates().get(i), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//                    LocalDate endDate = LocalDate.parse(profileDTO.getEndDates().get(i), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//                    experience.setStartDate(startDate);
//                    experience.setEndDate(endDate);
//
//                    experience.setMessage(profileDTO.getMessages().get(i));
//
//                    // Assign first technology if available
//                    if (profileDTO.getTechIds() != null && !profileDTO.getTechIds().isEmpty()) {
//                        Technology technology = technologyRepository.findById(profileDTO.getTechIds().get(0))
//                                .orElseThrow(() -> new RuntimeException("Technology not found with Id " + profileDTO.getTechIds().get(0)));
//                        experience.setTechnology(technology);
//                    }
//
//                    experienceRepository.save(experience);
//                }
//            }
//
//            return new ApiResponse<>(200, "Profile fields stored successfully for Id " + id, null);
//
//        } catch (DataAccessException ex) {
//            return new ApiResponse<>(500, "Database error occurred: " + ex.getMessage(), null);
//        } catch (Exception ex) {
//            return new ApiResponse<>(500, "An unexpected error occurred: " + ex.getMessage(), null);
//        }
//    }
//}

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

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public ApiResponse<String> addProfileFields(Long id, ProfileDTO profileDTO, MultipartFile profileImage) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with Id " + id));

            // Update basic user fields
            if (profileDTO.getFirstName() != null && !profileDTO.getFirstName().isEmpty()) {
                user.setFirstName(profileDTO.getFirstName());
            }
            if (profileDTO.getMiddleName() != null && !profileDTO.getMiddleName().isEmpty()) {
                user.setMiddleName(profileDTO.getMiddleName());
            }
            if (profileDTO.getLastName() != null && !profileDTO.getLastName().isEmpty()) {
                user.setLastName(profileDTO.getLastName());
            }

            // Parse and set DOB
            if (profileDTO.getDOB() != null && !profileDTO.getDOB().isEmpty()) {
                LocalDate dob = LocalDate.parse(profileDTO.getDOB(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                user.setDOB(dob);
            }

            // Set blood group
            if (profileDTO.getBloodGroup() != null && !profileDTO.getBloodGroup().isEmpty()) {
                user.setBloodGroup(profileDTO.getBloodGroup());
            }

            // Save profile image if provided
            if (profileImage != null && !profileImage.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
                String uploadDir = "D:/Duplicate/HRM (2)/HRM/uploads/profile-pictures/";

                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) uploadPath.mkdirs();

                File destinationFile = new File(uploadPath, fileName);
                profileImage.transferTo(destinationFile);

                // Save only relative URL/path for frontend consumption
                user.setProfilePicture("/uploads/profile-pictures/" + fileName);
            }

            // Update user's technologies
            if (profileDTO.getTechIds() != null && !profileDTO.getTechIds().isEmpty()) {
                // Delete old user technologies first
                userTechnologyRepository.deleteByUser(user);

                for (Long techId : profileDTO.getTechIds()) {
                    Technology technology = technologyRepository.findById(techId)
                            .orElseThrow(() -> new RuntimeException("Technology not found with Id " + techId));
                    UserTechnology userTechnology = new UserTechnology(user, technology);
                    userTechnologyRepository.save(userTechnology);
                }
            }

            // Save user after all updates
            userRepository.save(user);

            // === EDUCATION ===
            if (profileDTO.getCourseNames() != null &&
                    profileDTO.getInstituteNames() != null &&
                    profileDTO.getPassingYearMonth() != null) {

                int size = Math.min(
                        Math.min(profileDTO.getCourseNames().size(), profileDTO.getInstituteNames().size()),
                        profileDTO.getPassingYearMonth().size()
                );

                for (int i = 0; i < size; i++) {
                    Education education = new Education();
                    education.setUser(user);
                    education.setCourse_name(profileDTO.getCourseNames().get(i));
                    education.setInstitute_name(profileDTO.getInstituteNames().get(i));
                    education.setPassingYearMonth(profileDTO.getPassingYearMonth().get(i));
                    educationRepository.save(education);
                }
            }

            // === EXPERIENCE ===
            if (profileDTO.getCompanyNames() != null &&
                    profileDTO.getStartDates() != null &&
                    profileDTO.getEndDates() != null &&
                    profileDTO.getMessages() != null) {

                int size = Math.min(
                        Math.min(profileDTO.getCompanyNames().size(), profileDTO.getStartDates().size()),
                        Math.min(profileDTO.getEndDates().size(), profileDTO.getMessages().size())
                );

                for (int i = 0; i < size; i++) {
                    Experience experience = new Experience();
                    experience.setUser(user);
                    experience.setCompanyName(profileDTO.getCompanyNames().get(i));

                    LocalDate startDate = LocalDate.parse(profileDTO.getStartDates().get(i), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    LocalDate endDate = LocalDate.parse(profileDTO.getEndDates().get(i), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    experience.setStartDate(startDate);
                    experience.setEndDate(endDate);

                    experience.setMessage(profileDTO.getMessages().get(i));

                    // Assign first technology if available
                    if (profileDTO.getTechIds() != null && !profileDTO.getTechIds().isEmpty()) {
                        Technology technology = technologyRepository.findById(profileDTO.getTechIds().get(0))
                                .orElseThrow(() -> new RuntimeException("Technology not found with Id " + profileDTO.getTechIds().get(0)));
                        experience.setTechnology(technology);
                    }

                    experienceRepository.save(experience);
                }
            }

            return new ApiResponse<>(200, "Profile fields stored successfully for Id " + id, null);

        } catch (DataAccessException ex) {
            return new ApiResponse<>(500, "Database error occurred: " + ex.getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>(500, "An unexpected error occurred: " + ex.getMessage(), null);
        }
    }
}
