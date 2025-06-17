package SRMS.FinalProject.Controller;

import SRMS.FinalProject.DTO.OfferLetterDTO;
import SRMS.FinalProject.Repository.UserRepository;
import SRMS.FinalProject.Service.OfferLetterService;
import SRMS.FinalProject.UserEntity.OfferLetter;
import SRMS.FinalProject.UserEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/api/offer-letter")
public class OfferLetterController {

    @Autowired
    private OfferLetterService offerLetterService;
    @Autowired
    private UserRepository userRepository;

    // Generate offer letter and send email
    @PostMapping("/generate")
    public ResponseEntity<?> generateOffer(@RequestBody Map<String, Long> request) {
        try {
            Long userId = request.get("userId");
            if (userId == null) {
                return ResponseEntity.badRequest().body("Missing userId in request body");
            }
            OfferLetter letter = offerLetterService.generateOfferLetter(userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String email = user.getEmail();
            String message = "Offer letter has been sent to " + email;

            return ResponseEntity.ok(Map.of(
                    "id", letter.getId(),
                    "userId", letter.getUserId(),
                    "issuedDate", letter.getIssuedDate(),
                    "email", email,
                    "message", message
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating offer letter: " + e.getMessage());
        }
    }



    // Download offer letter file for given userId
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam Long userId) {
        try {
            Resource file = offerLetterService.download(userId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ByteArrayResource(("Offer letter not found for userId: " + userId).getBytes()));

        }
    }

}
