package SRMS.FinalProject.Repository;

import SRMS.FinalProject.UserEntity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Optional<Meeting> findByRoomName(String roomName);
    List<Meeting> findByRoomCreatedFalseAndScheduledTimeBefore(LocalDateTime time);
}
