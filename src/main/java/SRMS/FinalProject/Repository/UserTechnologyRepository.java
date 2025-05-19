package SRMS.FinalProject.Repository;

import SRMS.FinalProject.UserEntity.Technology;
import SRMS.FinalProject.UserEntity.User;
import SRMS.FinalProject.UserEntity.UserTechnology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//
@Repository
public interface UserTechnologyRepository extends JpaRepository<UserTechnology, Long> {

    boolean existsByUserAndTechnology(User user, Technology technology);

    List<UserTechnology> findByUserId(Long userId);

    void deleteByUser(User user);
}
