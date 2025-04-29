package SRMS.FinalProject.Service;
import SRMS.FinalProject.Repository.ForgotPasswordUserRepository;
import SRMS.FinalProject.UserEntity.ForgotPasswordUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordUserService {

    private final ForgotPasswordUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public ForgotPasswordUserService(ForgotPasswordUserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }


    public boolean sendResetEmail(String email) {
        Optional<ForgotPasswordUser> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            ForgotPasswordUser user = userOptional.get();


            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setTokenExpiry(LocalDateTime.now().plusMinutes(15)); // Token expires in 15 mins
            userRepository.save(user);

            String resetLink = "https://yourdomain.com/reset-password?token=" + token;

            // Send email
            emailService.sendemail2(email, "Password Reset", "Click the link to reset your password: " + resetLink + " (Valid for 15 minutes)");
            return true;
        }
        return false;
    }


    public boolean resetPassword(String token, String newPassword) {
        Optional<ForgotPasswordUser> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isPresent()) {
            ForgotPasswordUser user = userOptional.get();

            // Check if token is expired
            if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
                return false; // Token expired
            }

            // Update password and clear token
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setTokenExpiry(null);

            userRepository.save(user);
            return true;
        }
        return false;
    }
}