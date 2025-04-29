package SRMS.FinalProject.Service;



import SRMS.FinalProject.Repository.OtpRepository;
import SRMS.FinalProject.UserEntity.Otp;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    private static final int OTP_EXPIRATION_MINUTES = 2;

    public String generateOtp() {
        return String.valueOf(new Random().nextInt(990000) + 20000);
    }

    public void sendOtp(String email) throws MessagingException {
        String otp = generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES);

        Otp otpEntry = new Otp();
        otpEntry.setEmail(email);
        otpEntry.setOtp1(otp);
        otpEntry.setExpiryTime(expiryTime);
        otpRepository.save(otpEntry);

        String logoPath = "D:/HRM/src/main/resources/templates/logo/OtpLogo.png"; // Set the path to your company logo
        emailService.sendOtpEmail(email, otp, logoPath);
    }

    public boolean verifyOtp(String email, String otp) {
        List<Otp> otpEntries = otpRepository.findByEmail(email);

        for (Otp otpEntry : otpEntries) {
            if (otpEntry.getOtp1().equals(otp) && otpEntry.getExpiryTime().isAfter(LocalDateTime.now())) {
                otpRepository.delete(otpEntry);
                return true;
            }
        }
        return false;
    }
}
