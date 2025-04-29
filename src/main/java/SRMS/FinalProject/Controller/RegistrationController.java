package SRMS.FinalProject.Controller;

import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.RegisterRequest;
import SRMS.FinalProject.Repository.UserRepository;
import SRMS.FinalProject.Service.RegistrationService;
import SRMS.FinalProject.Service.OtpService;
import SRMS.FinalProject.UserEntity.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final OtpService otpService;
    private final UserRepository userRepository;

    public RegistrationController(RegistrationService registrationService, OtpService otpService, UserRepository userRepository) {
        this.registrationService = registrationService;
        this.otpService = otpService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(
            @Valid @RequestBody RegisterRequest request,
            BindingResult result) {

        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, errorMessage, null));
        }
        try {

            ApiResponse<String> response = registrationService.registerUser(request, request.getPassword());
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(500, "Registration failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<String>> resendOtp(@RequestBody @Valid Map<String, String> request) {
        String email = request.get("email");

        try {
            otpService.sendOtp(email); // Resend OTP with email
            return ResponseEntity.ok(new ApiResponse<>(200, "OTP sent successfully!", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Failed to send OTP: " + e.getMessage(), null));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@RequestBody @Valid Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        try {


            if (otpService.verifyOtp(email, otp)) {
                Optional<User> optionalUser = userRepository.findByEmail(email);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    user.setEmailVerified(true);
                    userRepository.save(user);
                    return ResponseEntity.ok(new ApiResponse<>(200, "Email verified successfully", null));
                } else {
                    return ResponseEntity.badRequest().body(new ApiResponse<>(400, "User not found for this email!", null));
                }
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Invalid or expired OTP!", null));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(500, "Error verifying OTP: " + e.getMessage(), null));
        }
    }
}
