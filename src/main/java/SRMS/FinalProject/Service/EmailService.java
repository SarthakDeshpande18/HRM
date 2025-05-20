package SRMS.FinalProject.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    //For Template
    public void sendEmail1(String to, String subject, String body, String logoPath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // true for HTML content
        helper.setFrom("sarthakdeshpande14@gmail.com");

        // Add the company logo as an inline image
        helper.addInline("OtpLogo", new File("D:/HRM/src/main/resources/templates/logo/OtpLogo.png")); // path to the logo image

        mailSender.send(message);
    }
    //Job Application Controller
        public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    public void sendemail2(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setFrom("sarthakdeshpande14@gmail.com"); // Replace with your actual sender email
            helper.setSubject(subject);
            helper.setText(body, true); // true for HTML content
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendOtpEmail(String email, String otpCode, String logoPath) throws MessagingException {
        String body = "<html>"
                + "<body>"
                + "<div style='font-family: Arial, sans-serif; color: #333;'>"
                + "<div style='text-align: center; margin-bottom: 50px;'>"
                + "<img src='cid:OtpLogo' alt='Company Logo' style='width: 200px; margin-bottom: 50px;'/>"
                + "<h1 style='color: #4CAF50;'>Weoto Welcomes You..!</h1>"
                + "<p style='font-size: 20px; font-weight: bold;'>To complete your registration, please use the OTP code below:</p>"
                + "<div style='font-size: 50px; font-weight: bold; color: #FF5722; text-align: center; margin: 30px 0;'>"
                + otpCode
                + "</div>"
                + "<p>This OTP code is valid for 2 minutes.</p>"
                + "<p>If you did not request this OTP, please ignore this email.</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        sendEmail1(email, "OTP Verification Code", body, logoPath);
    }
    public void sendEmailWithLogo(String to, String subject, String htmlBody, String logoPath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        FileSystemResource res = new FileSystemResource(new File(logoPath));
        helper.addInline("OtpLogo", res);

        mailSender.send(message);
    }

}

