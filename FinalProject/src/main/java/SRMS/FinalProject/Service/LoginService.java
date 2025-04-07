package SRMS.FinalProject.Service;

import SRMS.FinalProject.DTO.LoginRequest;
import SRMS.FinalProject.Repository.UserRepository;
import SRMS.FinalProject.UserEntity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
public boolean authenticateUser(@RequestBody @Valid LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail()).orElse(null);

    if (user == null) {
        throw new RuntimeException("User not Found!");
    }

    if (!user.isEmailVerified()) {
        throw new RuntimeException("Email is not verified. Please verify your email before logging in.");
    }

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new RuntimeException("Invalid credentials. Please check your email and password.");
    }

    return true;
}
}
