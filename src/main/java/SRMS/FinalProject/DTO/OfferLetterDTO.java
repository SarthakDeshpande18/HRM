package SRMS.FinalProject.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class OfferLetterDTO {
    private Long id;
    private Long userId;
    private LocalDate issuedDate;

    public OfferLetterDTO(Long id, Long userId, LocalDate issuedDate) {
        this.id = id;
        this.userId = userId;
        this.issuedDate = issuedDate;
    }

    // Getters and setters
}
