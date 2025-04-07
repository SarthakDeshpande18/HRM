package SRMS.FinalProject.Controller;


import SRMS.FinalProject.DTO.LoginRequest;
import SRMS.FinalProject.JwtUtil.JwtUtil;
import SRMS.FinalProject.Repository.UserRepository;

import SRMS.FinalProject.Service.LoginService;
import SRMS.FinalProject.Service.OtpService;
import SRMS.FinalProject.UserEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService authService;
    private final OtpService otpService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public LoginController(LoginService authService, OtpService otpService, UserRepository userRepository, JwtUtil jwtUtil) {
        this.authService = authService;
        this.otpService = otpService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                boolean isAuthenticated = authService.authenticateUser(request);
                if (isAuthenticated) {
                    if (!user.isEmailVerified()) {
                        return ResponseEntity.badRequest().body("Email not verified");
                    }
                    String token = jwtUtil.generateToken(user.getEmail());
                    return ResponseEntity.ok(Map.of("token", token));
                }
            }
            return ResponseEntity.badRequest().body("Invalid credentials");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
