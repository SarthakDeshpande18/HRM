package SRMS.FinalProject.Service;


import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.RegisterRequest;
import SRMS.FinalProject.Repository.RoleRepository;
import SRMS.FinalProject.Repository.UserRepository;
import SRMS.FinalProject.Repository.UserRoleRepository;
import SRMS.FinalProject.Repository.UserRoleRepository1;
import SRMS.FinalProject.UserEntity.Role;
import SRMS.FinalProject.UserEntity.User;
import SRMS.FinalProject.UserEntity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository1 userRoleRepository1;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private OtpService otpService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public ApiResponse<String> registerUser(RegisterRequest request, String password) {
        try {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return new ApiResponse<>(400, "Email is already registered!", null);
            }

            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                return new ApiResponse<>(400, "Username is already taken!", null);
            }

            if (userRepository.findBymobileNo(String.valueOf(request.getMobile_no())).isPresent()) {
                return new ApiResponse<>(400, "Mobile Number is already registered!", null);
            }

            // Fetch USER role from Role table
            Role userRole = roleRepository.findByRoleName("USER")
                    .orElseThrow(() -> new RuntimeException("Default USER role not found. Please insert it in Role table."));

            // Create new user
            User user = new User();
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(password));
            user.setMobileNo(String.valueOf(request.getMobile_no()));
            user.setEmailVerified(false);

            userRepository.save(user);

            // Assign USER role
            UserRole userRoleEntry = new UserRole();
            userRoleEntry.setUser(user);
            userRoleEntry.setRole(userRole);
            userRoleRepository.save(userRoleEntry);

            otpService.sendOtp(user.getEmail());

            return new ApiResponse<>(200, "User registered successfully. OTP sent to email.", null);
        } catch (DataAccessException ex) {
            System.err.println("Database error occurred: " + ex.getMessage());
            return new ApiResponse<>(500, "Registration failed due to database error. Please try again later.", null);
        } catch (Exception ex) {
            System.err.println("An unexpected error occurred: " + ex.getMessage());
            return new ApiResponse<>(500, "An unexpected error occurred during registration.", null);
        }
    }
}
