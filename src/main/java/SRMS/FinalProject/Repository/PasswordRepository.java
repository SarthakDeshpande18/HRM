package SRMS.FinalProject.Repository;
import SRMS.FinalProject.UserEntity.ForgotPasswordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface PasswordRepository {
    public interface UserRepository extends JpaRepository<ForgotPasswordUser, Long> {
        Optional<ForgotPasswordUser> findByEmail(String email);
    }
}