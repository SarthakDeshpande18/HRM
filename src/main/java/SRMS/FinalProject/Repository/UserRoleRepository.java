package SRMS.FinalProject.Repository;



import SRMS.FinalProject.DTO.UserRoleResponse;
import SRMS.FinalProject.UserEntity.Role;
import SRMS.FinalProject.UserEntity.User;
import SRMS.FinalProject.UserEntity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Transactional
    void deleteByUser(User user); // Deletes all roles for the given user
    Optional<UserRole> findByUserAndRole(User user, Role role); // To check if role already exists for user
}
