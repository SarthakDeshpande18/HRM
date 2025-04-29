package SRMS.FinalProject.Repository;

import SRMS.FinalProject.UserEntity.ForgotPasswordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordUserRepository extends JpaRepository<ForgotPasswordUser, Long> {
    Optional<ForgotPasswordUser> findByEmail(String email);
    Optional<ForgotPasswordUser> findByResetToken(String token);  // 🔹 New method
}
