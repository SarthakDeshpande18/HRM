package SRMS.FinalProject.Service;

import SRMS.FinalProject.Repository.OfferLetterRepository;
import SRMS.FinalProject.Repository.UserRepository;
import SRMS.FinalProject.UserEntity.OfferLetter;
import SRMS.FinalProject.UserEntity.User;
import SRMS.FinalProject.UserEntity.Role;
import com.itextpdf.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Document;
import java.io.FileOutputStream;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OfferLetterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferLetterRepository offerLetterRepository;

    @Autowired
    private JavaMailSender mailSender;
    MediaType mediaType = MediaType.APPLICATION_PDF;

    public OfferLetter generateOfferLetter(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fullName = Stream.of(user.getFirstName(), user.getMiddleName(), user.getLastName())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));

        String role = user.getRoles().stream()
                .findFirst()
                .map(Role::getRoleName)
                .orElse("Employee");

        LocalDate issuedDate = LocalDate.now();

        // Generate offer letter content
        String content = String.format("""
                Dear %s,

                We are pleased to offer you the position of %s at our company.

                Please let us know if you have any questions.

                Regards,
                HR Department
                """, fullName, role, issuedDate);


        // Save as PDF
        String folderPath = "uploads/offer_letters/";
        Files.createDirectories(Paths.get(folderPath));
        String fileName = "offer_letter_" + userId + ".pdf";
        String filePath = folderPath + fileName;

        // Create PDF content
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        document.add(new Paragraph("Dear " + fullName + ","));
        document.add(new Paragraph("We are pleased to offer you the position of " + role + "."));
        document.add(new Paragraph("Start Date: " + issuedDate));
        document.add(new Paragraph("Please let us know if you have any questions."));
        document.add(new Paragraph("Regards,\nHR Department"));

        document.close();

        // Save to DB
        OfferLetter letter = new OfferLetter();
        letter.setUserId(userId);
        letter.setFilePath(filePath);
        letter.setIssuedDate(issuedDate);
        offerLetterRepository.save(letter);

        // Send Email
        sendOfferLetterEmail(user.getEmail(), fullName, filePath);

        return letter;
    }

    private void sendOfferLetterEmail(String toEmail, String name, String filePath) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Your Offer Letter");
        helper.setText("Dear " + name + "\n\nPlease find your offer letter attached.\n\nRegards,\nHR Department");

        FileSystemResource file = new FileSystemResource(new File(filePath));
        helper.addAttachment("OfferLetter.pdf", file);


        mailSender.send(message);
    }
    public Resource download(Long userId) throws Exception {
        String folderPath = "uploads/offer_letters/";
        String fileName = "offer_letter_" + userId + ".pdf";
        Path path = Paths.get(folderPath + fileName);
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read file: " + path.toString());
        }
    }

}
