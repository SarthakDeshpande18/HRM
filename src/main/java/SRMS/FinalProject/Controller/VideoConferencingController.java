package SRMS.FinalProject.Controller;

import SRMS.FinalProject.DTO.ScheduleMeetingRequest;
import SRMS.FinalProject.Repository.MeetingRepository;
import SRMS.FinalProject.UserEntity.Meeting;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VideoGrant;
import com.twilio.rest.video.v1.Room;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/video")
@CrossOrigin(origins = "*")
public class VideoConferencingController {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.api.key}")
    private String apiKey;

    @Value("${twilio.api.secret}")
    private String apiSecret;

    @Autowired
    private MeetingRepository meetingRepo;

    @PostMapping("/schedule-meeting")
    public ResponseEntity<?> scheduleMeeting(@RequestBody ScheduleMeetingRequest request, HttpServletRequest httpRequest) {
        Meeting meeting = new Meeting();
        meeting.setRoomName(request.getRoomName());
        meeting.setScheduledTime(request.getScheduledTime());
        meetingRepo.save(meeting);

        String baseUrl = httpRequest.getRequestURL().toString().replace(httpRequest.getRequestURI(), httpRequest.getContextPath());
        String joinUrl = baseUrl + "/join-room?roomName=" + request.getRoomName();

        return ResponseEntity.ok(Map.of(
                "message", "Meeting scheduled",
                "roomName", request.getRoomName(),
                "joinUrl", joinUrl
        ));
    }

    @PostMapping("/create-room")
    public ResponseEntity<?> createRoom(@RequestParam String roomName, HttpServletRequest request) {
        Optional<Meeting> optionalMeeting = meetingRepo.findByRoomName(roomName);


        if (optionalMeeting.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Cannot create room. No scheduled meeting found with this room name."));
        }
        Meeting meeting = optionalMeeting.get();

        if (meeting.isRoomCreated()) {
            String joinUrl = getJoinUrl(request, roomName);
            return ResponseEntity.ok(Map.of(
                    "roomSid", meeting.getRoomSid(),
                    "roomName", roomName,
                    "joinUrl", joinUrl
            ));
        }

        Room room = Room.creator()
                .setUniqueName(roomName)
                .setType(Room.RoomType.GROUP)
                .setEmptyRoomTimeout(50)
                .create();

        meeting.setRoomSid(room.getSid());
        meeting.setRoomCreated(true);
        meetingRepo.save(meeting);

        String joinUrl = getJoinUrl(request, roomName);
        return ResponseEntity.ok(Map.of(
                "roomSid", room.getSid(),
                "roomName", roomName,
                "joinUrl", joinUrl
        ));
    }

    private String getJoinUrl(HttpServletRequest request, String roomName) {
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        return baseUrl + "/join-room?roomName=" + roomName;
    }


    @GetMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestParam String identity, @RequestParam String roomName) {
        Optional<Meeting> optionalMeeting = meetingRepo.findByRoomName(roomName);

        if (optionalMeeting.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Room does not exist. Please use a valid scheduled meeting link."));
        }

        Meeting meeting = optionalMeeting.get();

        if (!meeting.isRoomCreated()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Room is not active yet. Please wait for it to be created."));
        }

        VideoGrant grant = new VideoGrant().setRoom(roomName);
        AccessToken token = new AccessToken.Builder(accountSid, apiKey, apiSecret)
                .identity(identity)
                .grant(grant)
                .build();

        return ResponseEntity.ok(Map.of("token", token.toJwt()));
    }

}
