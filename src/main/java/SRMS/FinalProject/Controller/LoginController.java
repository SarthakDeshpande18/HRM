package SRMS.FinalProject.Controller;

import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.LoginRequest;
import SRMS.FinalProject.DTO.UserDto;
import SRMS.FinalProject.JwtUtil.JwtUtil;
import SRMS.FinalProject.Repository.UserRepository;
import SRMS.FinalProject.Service.LoginService;
import SRMS.FinalProject.Service.OtpService;
import SRMS.FinalProject.Service.UserService;
import SRMS.FinalProject.UserEntity.Role;
import SRMS.FinalProject.UserEntity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
//
//    @RestController
//    @RequestMapping("/api/auth")
//    public class LoginController {
//
//        private final LoginService authService;
//        private final OtpService otpService;
//        private final UserRepository userRepository;
//        private final JwtUtil jwtUtil;
//
//        //
//        @Autowired
//        public LoginController(LoginService authService, OtpService otpService, UserRepository userRepository, JwtUtil jwtUtil) {
//            this.authService = authService;
//            this.otpService = otpService;
//            this.userRepository = userRepository;
//            this.jwtUtil = jwtUtil;
//        }
//
//        @PostMapping("/login")
//        public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//            try {
//                Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
//                if (userOptional.isPresent()) {
//                    User user = userOptional.get();
//                    boolean isAuthenticated = authService.authenticateUser(request);
//                    if (isAuthenticated) {
//                        if (!user.isEmailVerified()) {
//                            return ResponseEntity.badRequest().body(Map.of(
//                                    "status", "error",
//                                    "message", "Email not verified",
//                                    "data", null
//                            ));
//                        }
//
//                        List<String> roleNames = user.getRoles().stream()
//                                .map(Role::getRoleName)
//                                .toList();
//
//                        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), roleNames, );
//
//                        return ResponseEntity.ok(Map.of(
//                                "token", token
//                        ));
//
//
//                    }
//                }
//                return ResponseEntity.badRequest().body(Map.of(
//                        "status", "error",
//                        "message", "Invalid credentials",
//                        "data", null
//                ));
//            } catch (RuntimeException e) {
//                return ResponseEntity.badRequest().body(Map.of(
//                        "status", "error",
//                        "message", e.getMessage(),
//                        "data", null
//                        ));
//    }
//        }
//
//    }



@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService authService;
    private final OtpService otpService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public LoginController(LoginService authService, OtpService otpService, UserRepository userRepository, JwtUtil jwtUtil, UserService userService) {
        this.authService = authService;
        this.otpService = otpService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "error",
                        "message", "User not found",
                        "data", null
                ));
            }

            authService.authenticateUser(request); // Will throw exception if invalid

            User user = userOptional.get();

            List<String> roleNames = user.getRoles().stream()
                    .map(Role::getRoleName)
                    .toList();

            String token = jwtUtil.generateToken(user.getEmail(), user.getId(), roleNames, user.getUsername());

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Login successful",
                    "token", token
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Email is not verified. Please verify your email before logging in.",
                    "data", null
            ));
        }

    }

    @PostMapping("/getById")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@RequestBody UserDto dto) {
        Long id = dto.getId();
        UserDto userDto = userService.getUserById(id);

        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "User with id " + id + " not found", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, "User fetched successfully", userDto));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(HttpServletRequest request) {
        try {
            String token = jwtUtil.extractToken(request);

            Long userId = jwtUtil.extractUserId(token);

            UserDto userDto = userService.getUserById(userId);
            if (userDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "User not found", null));
            }

            return ResponseEntity.ok(new ApiResponse<>(200, "User fetched successfully", userDto));

        } catch (Exception e) {
            e.printStackTrace(); // Log the exact error
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Invalid or expired token", null));
        }
    }
}
