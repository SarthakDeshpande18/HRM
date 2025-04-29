package SRMS.FinalProject.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Getter
@Setter
@Table(name = "users")
public class ForgotPasswordUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private boolean emailVerified;

    private String resetToken;  // 🔹 Token for password reset
    private LocalDateTime tokenExpiry;  // 🔹 Expiry time for reset token
}
