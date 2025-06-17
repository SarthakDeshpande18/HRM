//package SRMS.FinalProject.Controller;
//
//import SRMS.FinalProject.Service.CalenderService;
//import SRMS.FinalProject.UserEntity.Calender;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/calendar")
//@CrossOrigin(origins = "*")
//public class CalenderController {
//
//    @Autowired
//    private CalenderService calenderService;
//
//    // GET events for a specific user
//    @GetMapping("/user/{userId}")
//    public List<Calender> getEventsForUser(@PathVariable Long userId) {
//        return calenderService.getEventsForUser(userId);
//    }
//
//    // POST: create event and send mail to members
//    @PostMapping("/create")
//    public Calender createEvent(@RequestBody Map<String, Object> request) {
//        // Extract Calender object from request
//        Calender calender = new Calender();
//        calender.setTitle((String) request.get("title"));
//        calender.setDescription((String) request.get("description"));
//        calender.setLocation((String) request.get("location"));
//        calender.setRecurrence((String) request.get("recurrence"));
//        calender.setTechnology((String) request.get("technology"));
//
//        // Parse LocalDateTime
//        calender.setStartTime(java.time.LocalDateTime.parse((String) request.get("startTime")));
//        calender.setEndTime(java.time.LocalDateTime.parse((String) request.get("endTime")));
//
//        // Handle createdBy field
//        Map<String, Object> createdByMap = (Map<String, Object>) request.get("createdBy");
//        if (createdByMap != null && createdByMap.get("id") != null) {
//            SRMS.FinalProject.UserEntity.User createdBy = new SRMS.FinalProject.UserEntity.User();
//            createdBy.setId(Long.valueOf(createdByMap.get("id").toString()));
//            calender.setCreatedBy(createdBy);
//        }
//
//        // Extract members list
//        List<String> members = (List<String>) request.get("members");
//
//        // Call service
//        return calenderService.createEvent(calender, members);
//    }
//
//    // PUT: update an event
//    @PutMapping("/update/{id}")
//    public Calender updateEvent(@PathVariable Long id, @RequestBody Calender updatedCalender) {
//        return calenderService.updateEvent(id, updatedCalender);
//    }
//
//    // DELETE: delete an event
//    @DeleteMapping("/delete/{id}")
//    public void deleteEvent(@PathVariable Long id) {
//        calenderService.deleteEvent(id);
//    }
//}
package SRMS.FinalProject.Controller;

import SRMS.FinalProject.Service.CalenderService;
import SRMS.FinalProject.UserEntity.Calender;
import SRMS.FinalProject.UserEntity.User;
import SRMS.FinalProject.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calender")
@CrossOrigin(origins = "*")
public class CalenderController {

    @Autowired
    private CalenderService calenderService;

    @Autowired
    private UserRepository userRepository;

    // ✅ Create new event (meeting) with members
    @PostMapping("/create")
    public Calender createEvent(@RequestBody Calender calender) {
        // Resolve full User objects for members if only IDs were passed
        if (calender.getMembers() != null && !calender.getMembers().isEmpty()) {
            List<User> fullMembers = calender.getMembers().stream()
                    .map(member -> userRepository.findById(member.getId())
                            .orElseThrow(() -> new RuntimeException("User not found with ID: " + member.getId())))
                    .toList();
            calender.setMembers(fullMembers);
        }

        return calenderService.createEvent(calender);
    }

    @PostMapping("/user")
    public List<Calender> getEventsForUser(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        if (userId == null) {
            throw new IllegalArgumentException("userId must be provided in request body");
        }
        return calenderService.getEventsForUser(userId);
    }


    @PutMapping("/update")
    public Calender updateEvent(@RequestBody Calender updatedCalender) {
        if (updatedCalender.getId() == null) {
            throw new IllegalArgumentException("ID must be provided in request body");
        }
        return calenderService.updateEvent(updatedCalender.getId(), updatedCalender);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteEvent(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");
        if (id == null) {
            return ResponseEntity.badRequest().body("ID is required in request body");
        }
        calenderService.deleteEvent(id);
        return ResponseEntity.ok("Event deleted successfully");
    }

}
