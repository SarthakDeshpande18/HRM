package SRMS.FinalProject.Service;

import SRMS.FinalProject.Repository.MeetingRepository;
import SRMS.FinalProject.UserEntity.Meeting;
import com.twilio.Twilio;
import com.twilio.rest.video.v1.Room;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomCleanupService {

    private final MeetingRepository meetingRepository;

    public RoomCleanupService(MeetingRepository meetingRepository,
                              @Value("${twilio.account.sid}") String accountSid,
                              @Value("${twilio.api.key}") String apiKey,
                              @Value("${twilio.api.secret}") String apiSecret) {
        this.meetingRepository = meetingRepository;
        Twilio.init(apiKey, apiSecret, accountSid); // 🔐 Auth
    }

    // ⏱ Runs every 1 minute
    @Scheduled(fixedRate = 60000)
    public void cleanupEndedRooms() {
        List<Meeting> meetings = meetingRepository.findAll();

        for (Meeting meeting : meetings) {
            if (!meeting.isRoomCreated() || meeting.getRoomSid() == null) continue;

            try {
                Room room = Room.fetcher(meeting.getRoomSid()).fetch();

                if ("completed".equals(room.getStatus().toString().toLowerCase())) {
                    meetingRepository.delete(meeting);
                    System.out.println("✅ Cleaned up room: " + meeting.getRoomName());
                }
            } catch (Exception e) {
                System.err.println("⚠️ Failed to fetch/delete room: " + meeting.getRoomName() + " | " + e.getMessage());
            }
        }
    }
}
