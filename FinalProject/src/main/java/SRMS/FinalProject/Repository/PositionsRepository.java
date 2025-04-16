package SRMS.FinalProject.Repository;

import SRMS.FinalProject.UserEntity.Positions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionsRepository extends JpaRepository<Positions, Long> {
    boolean existsByPositionName(String positionName);
}