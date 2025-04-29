package SRMS.FinalProject.Controller;

import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.PasswordRequest;
import SRMS.FinalProject.DTO.ResetPasswordRequest;
import SRMS.FinalProject.Service.ForgotPasswordUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class PasswordController {

    private final ForgotPasswordUserService userService;

    public PasswordController(ForgotPasswordUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody PasswordRequest request) {
        boolean emailSent = userService.sendResetEmail(request.getEmail());
        if (!emailSent) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(404, "Email not found", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Password reset link sent", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean updated = userService.resetPassword(request.getToken(), request.getNewPassword());
        if (!updated) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(404, "Invalid or expired token", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Password reset successfully", null));
    }
}