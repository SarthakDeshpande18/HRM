package SRMS.FinalProject.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleMeetingRequest {
    private String roomName;
    private LocalDateTime scheduledTime;
}
