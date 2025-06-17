package SRMS.FinalProject.Repository;

import SRMS.FinalProject.UserEntity.OfferLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfferLetterRepository extends JpaRepository<OfferLetter, Long> {
    Optional<OfferLetter> findByUserId(Long userId);
}
