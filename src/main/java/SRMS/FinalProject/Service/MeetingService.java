package SRMS.FinalProject.Service;

import SRMS.FinalProject.Repository.MeetingRepository;
import SRMS.FinalProject.UserEntity.Meeting;
import com.twilio.rest.video.v1.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepo;

    @Scheduled(fixedRate = 60000) // every 1 min
    public void activateRooms() {
        List<Meeting> meetings = meetingRepo.findByRoomCreatedFalseAndScheduledTimeBefore(LocalDateTime.now().plusMinutes(5));
        for (Meeting meeting : meetings) {
            Room room = Room.creator()
                    .setUniqueName(meeting.getRoomName())
                    .setType(Room.RoomType.GROUP)
                    .create();

            meeting.setRoomSid(room.getSid());
            meeting.setRoomCreated(true);
            meetingRepo.save(meeting);
        }
    }
}
