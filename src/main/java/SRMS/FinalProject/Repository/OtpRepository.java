package SRMS.FinalProject.Repository;



import SRMS.FinalProject.UserEntity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    List<Otp> findByEmail(String email);
}