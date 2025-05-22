package SRMS.FinalProject.Repository;

import SRMS.FinalProject.DTO.UserTechnologyDTO;
import SRMS.FinalProject.UserEntity.Technology;
import SRMS.FinalProject.UserEntity.User;
import SRMS.FinalProject.UserEntity.UserTechnology;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//
@Repository
public interface UserTechnologyRepository extends JpaRepository<UserTechnology, Long> {

    boolean existsByUserAndTechnology(User user, Technology technology);

    List<UserTechnology> findByUserId(Long userId);

    void deleteByUser(User user);

    @Query("SELECT new SRMS.FinalProject.DTO.UserTechnologyDTO(u.id, u.technology.techName, u.user.id, u.technology.tech_id) FROM UserTechnology u")
    Page<UserTechnologyDTO> findAllUserTechIds(Pageable pageable);

    @Query("SELECT new SRMS.FinalProject.DTO.UserTechnologyDTO(u.id, u.technology.techName, u.user.id, u.technology.tech_id) FROM UserTechnology u WHERE u.user.id = :userId")
    Page<UserTechnologyDTO> findByUserIdCustom(@Param("userId") Long userId, Pageable pageable);



}

