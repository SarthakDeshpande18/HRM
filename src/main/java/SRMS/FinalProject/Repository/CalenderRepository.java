//package SRMS.FinalProject.Repository;
//
//import SRMS.FinalProject.UserEntity.Calender;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface CalenderRepository extends JpaRepository<Calender, Long> {
//    List<Calender> findByCreatedById(Long userId);
//}


package SRMS.FinalProject.Repository;

import SRMS.FinalProject.UserEntity.Calender;
import SRMS.FinalProject.UserEntity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalenderRepository extends JpaRepository<Calender, Long> {
    List<Calender> findByCreatedById(Long userId);

}
