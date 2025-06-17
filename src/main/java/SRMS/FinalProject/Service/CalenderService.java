//package SRMS.FinalProject.Service;
//
//import SRMS.FinalProject.Repository.CalenderRepository;
//import SRMS.FinalProject.UserEntity.Calender;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CalenderService {
//    @Autowired
//    private CalenderRepository calenderRepository;
//
//    public List<Calender> getEventsForUser(Long userId) {
//        return calenderRepository.findByCreatedById(userId);
//    }
//
//    public Calender createEvent(Calender calender) {
//        return calenderRepository.save(calender);
//    }
//
//    public Calender updateEvent(Long id, Calender updatedCalender) {
//        Calender existing = calenderRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Event not found"));
//        existing.setTitle(updatedCalender.getTitle());
//        existing.setDescription(updatedCalender.getDescription());
//        existing.setStartTime(updatedCalender.getStartTime());
//        existing.setEndTime(updatedCalender.getEndTime());
//        existing.setTechnology(updatedCalender.getTechnology());
//        existing.setLocation(updatedCalender.getLocation());
//        existing.setRecurrence(updatedCalender.getRecurrence());
//        return calenderRepository.save(existing);
//    }
//
//    public void deleteEvent(Long id) {
//        calenderRepository.deleteById(id);
//    }
//}

package SRMS.FinalProject.Service;

import SRMS.FinalProject.Repository.CalenderRepository;
import SRMS.FinalProject.Repository.UserRepository;
import SRMS.FinalProject.UserEntity.Calender;
import SRMS.FinalProject.UserEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalenderService {

    @Autowired
    private CalenderRepository calenderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public List<Calender> getEventsForUser(Long userId) {
        return calenderRepository.findByCreatedById(userId);
    }

    public Calender createEvent(Calender calender) {
        Calender savedEvent = calenderRepository.save(calender);

        if (savedEvent.getMembers() != null) {
            for (User member : savedEvent.getMembers()) {
                User fullUser = userRepository.findById(member.getId())
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + member.getId()));

                if (fullUser.getEmail() != null && !fullUser.getEmail().isEmpty()) {
                    sendMeetingMail(fullUser, savedEvent);
                }
            }
        }

        return savedEvent;
    }

    public Calender updateEvent(Long id, Calender updatedCalender) {
        Calender existing = calenderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        existing.setTitle(updatedCalender.getTitle());
        existing.setDescription(updatedCalender.getDescription());
        existing.setStartTime(updatedCalender.getStartTime());
        existing.setEndTime(updatedCalender.getEndTime());
        existing.setTechnology(updatedCalender.getTechnology());
        existing.setLocation(updatedCalender.getLocation());
//        existing.setRecurrence(updatedCalender.getRecurrence());
        existing.setMembers(updatedCalender.getMembers());

        return calenderRepository.save(existing);
    }

    public void deleteEvent(Long id) {
        calenderRepository.deleteById(id);
    }

    private void sendMeetingMail(User user, Calender event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Meeting Invitation: " + event.getTitle());
        message.setText("Dear " + user.getFirstName() + ",\n\n"
                + "You are invited to a meeting:\n"
                + "Title: " + event.getTitle() + "\n"
                + "Description: " + event.getDescription() + "\n"
                + "Location: " + event.getLocation() + "\n"
                + "Technology: " + event.getTechnology() + "\n"
                + "Start Time: " + event.getStartTime() + "\n"
                + "End Time: " + event.getEndTime() + "\n\n"
                + "Regards,\nSRMS Team");
        mailSender.send(message);
    }
}
