package SRMS.FinalProject.Repository;

import SRMS.FinalProject.UserEntity.Positions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionsRepository extends JpaRepository<Positions, Long> {

    // Method to check if a position with the same name exists
    boolean existsByPositionName(String positionName);

    // Optionally, add a custom query to find a position by name (if needed)
    Optional<Positions> findByPositionName(String positionName);
}
