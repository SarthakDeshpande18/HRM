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
        if (optionalMeeting.isPresent()) {
            Meeting meeting = optionalMeeting.get();
            if (meeting.isRoomCreated()) {
                return ResponseEntity.ok(Map.of(
                        "roomSid", meeting.getRoomSid(),
                        "roomName", roomName,
                        "joinUrl", request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath())
                                + "/join-room?roomName=" + roomName
                ));
            } else {
                Room room = Room.creator()
                        .setUniqueName(roomName)
                        .setType(Room.RoomType.GROUP)
                        .create();

                meeting.setRoomSid(room.getSid());
                meeting.setRoomCreated(true);
                meetingRepo.save(meeting);

                String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
                String joinUrl = baseUrl + "/join-room?roomName=" + roomName;

                return ResponseEntity.ok(Map.of(
                        "roomSid", room.getSid(),
                        "roomName", roomName,
                        "joinUrl", joinUrl
                ));
            }
        }

        // fallback for instant meetings (if not scheduled)
        Room room = Room.creator()
                .setUniqueName(roomName)
                .setType(Room.RoomType.GROUP)
                .create();

        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        String joinUrl = baseUrl + "/join-room?roomName=" + roomName;

        return ResponseEntity.ok(Map.of(
                "roomSid", room.getSid(),
                "roomName", room.getUniqueName(),
                "joinUrl", joinUrl
        ));
    }

    @GetMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestParam String identity, @RequestParam String roomName) {
        Optional<Meeting> optionalMeeting = meetingRepo.findByRoomName(roomName);

        if (optionalMeeting.isPresent() && !optionalMeeting.get().isRoomCreated()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Room is not active yet. Please wait for the scheduled time."));
        }

        VideoGrant grant = new VideoGrant().setRoom(roomName);
        AccessToken token = new AccessToken.Builder(accountSid, apiKey, apiSecret)
                .identity(identity)
                .grant(grant)
                .build();
        return ResponseEntity.ok(Map.of("token", token.toJwt()));
    }
}
